package ru.cft.focusstart.service.weapon;

import ru.cft.focusstart.api.dto.WeaponDto;
import ru.cft.focusstart.entity.Instructor;
import ru.cft.focusstart.entity.Weapon;
import ru.cft.focusstart.exception.ObjectNotFoundException;
import ru.cft.focusstart.mapper.WeaponMapper;
import ru.cft.focusstart.repository.instructor.InstructorRepository;
import ru.cft.focusstart.repository.instructor.JdbcInstructorRepository;
import ru.cft.focusstart.repository.weapon.JdbcWeaponRepository;
import ru.cft.focusstart.repository.weapon.WeaponRepository;
import ru.cft.focusstart.service.validation.Validator;

import java.util.List;
import java.util.stream.Collectors;

public class DefaultWeaponService implements WeaponService {

    private static final DefaultWeaponService INSTANCE = new DefaultWeaponService();

    private final InstructorRepository instructorRepository = JdbcInstructorRepository.getInstance();
    private final WeaponRepository weaponRepository = JdbcWeaponRepository.getInstance();
    private final WeaponMapper weaponMapper = WeaponMapper.getInstance();

    private DefaultWeaponService() {}

    public static WeaponService getInstance() {
        return INSTANCE;
    }

    @Override
    public List<WeaponDto> get(String type, String model, String fullNameInstructor) {
        return weaponRepository.get(type, model, fullNameInstructor)
                .stream()
                .map(weaponMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public WeaponDto getById(Long id) {
        Validator.checkNotNull("id", id);

        Weapon weapon = getWeapon(id);

        return weaponMapper.toDto(weapon);
    }

    private Weapon getWeapon(Long id) {
        return weaponRepository.getById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Weapon with id %s not found", id)));
    }

    @Override
    public WeaponDto create(WeaponDto weaponDto) {
        validate(weaponDto);

        Weapon weapon = add(null, weaponDto);

        return weaponMapper.toDto(weapon);
    }

    private void validate(WeaponDto weaponDto) {
        Validator.checkNull("weapon.id", weaponDto.getId());
        Validator.checkNotNull("weapon.instructorId", weaponDto.getInstructorId());
        Validator.checkSize("weapon.type", weaponDto.getType(), 1, 100);
        Validator.checkSize("weapon.model", weaponDto.getModel(), 1, 100);
        Validator.checkNotNull("weapon.number", weaponDto.getNumber());
    }

    private Weapon add(Long id, WeaponDto weaponDto) {
        Instructor instructor = getInstructor(weaponDto.getInstructorId());
        Weapon weapon = new Weapon();
        weapon.setId(id);
        weapon.setInstructor(instructor);
        weapon.setType(weaponDto.getType());
        weapon.setModel(weaponDto.getModel());
        weapon.setSeries(weaponDto.getSeries() != null ? weaponDto.getSeries() : "");
        weapon.setNumber(weaponDto.getNumber());

        weaponRepository.add(weapon);

        return weapon;
    }

    private Instructor getInstructor(Long id) {
        return instructorRepository.getById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Instructor with id %s not found", id)));
    }

    @Override
    public WeaponDto merge(Long id, WeaponDto weaponDto) {
        Validator.checkNotNull("id", id);
        validate(weaponDto);

        Weapon weapon = weaponRepository.getById(id)
                .map(existing -> update(existing, weaponDto))
                .orElseGet(() -> add(id, weaponDto));

        return weaponMapper.toDto(weapon);
    }

    private Weapon update(Weapon weapon, WeaponDto weaponDto) {
        weapon.setType(weaponDto.getType());
        weapon.setModel(weaponDto.getModel());
        weapon.setSeries(weaponDto.getSeries() != null ? weaponDto.getSeries() : "");
        weapon.setNumber(weaponDto.getNumber());
        if (!weaponDto.getInstructorId().equals(weapon.getInstructor().getId())) {
            Instructor instructor = getInstructor(weaponDto.getInstructorId());
            weapon.setInstructor(instructor);
        }

        weaponRepository.update(weapon);

        return weapon;
    }
}
