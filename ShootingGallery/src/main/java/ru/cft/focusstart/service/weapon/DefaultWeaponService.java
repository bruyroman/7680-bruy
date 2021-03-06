package ru.cft.focusstart.service.weapon;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cft.focusstart.api.dto.WeaponDto;
import ru.cft.focusstart.entity.Instructor;
import ru.cft.focusstart.entity.Weapon;
import ru.cft.focusstart.exception.ObjectNotFoundException;
import ru.cft.focusstart.mapper.WeaponMapper;
import ru.cft.focusstart.repository.instructor.InstructorRepository;
import ru.cft.focusstart.repository.weapon.WeaponRepository;
import ru.cft.focusstart.service.validation.Validator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultWeaponService implements WeaponService {

    private final InstructorRepository instructorRepository;
    private final WeaponRepository weaponRepository;
    private final WeaponMapper weaponMapper;

    @Override
    @Transactional(readOnly = true)
    public List<WeaponDto> get(String type, String model, String fullNameInstructor) {
        return weaponRepository.find(type == null ? "" : type, model == null ? "" : model, fullNameInstructor == null ? "" : fullNameInstructor)
                .stream()
                .map(weaponMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public WeaponDto getById(Long id) {
        Validator.checkNotNull("id", id);

        Weapon weapon = getWeapon(id);

        return weaponMapper.toDto(weapon);
    }

    private Weapon getWeapon(Long id) {
        return weaponRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Weapon with id %s not found", id)));
    }

    @Override
    @Transactional
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

        return weaponRepository.save(weapon);
    }

    private Instructor getInstructor(Long id) {
        return instructorRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Instructor with id %s not found", id)));
    }

    @Override
    @Transactional
    public WeaponDto merge(Long id, WeaponDto weaponDto) {
        Validator.checkNotNull("id", id);
        validate(weaponDto);

        Weapon weapon = weaponRepository.findById(id)
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

        return weapon;
    }
}
