package ru.cft.focusstart.service.weapon;

import ru.cft.focusstart.api.dto.WeaponDto;
import ru.cft.focusstart.service.validation.Validator;

import java.util.List;

public class DefaultWeaponService implements WeaponService {

    private static final DefaultWeaponService INSTANCE = new DefaultWeaponService();

    //TODO: Экземпляры репозиториев

    private DefaultWeaponService() {}

    public static WeaponService getInstance() {
        return INSTANCE;
    }

    @Override
    public List<WeaponDto> get(String type, String model, String fullNameInstructor) {
        return null;
    }

    @Override
    public WeaponDto getById(Long id) {
        Validator.checkNotNull("id", id);

        return null;
    }

    @Override
    public WeaponDto create(WeaponDto weaponDto) {
        validation(weaponDto);

        return null;
    }

    private void validation(WeaponDto weaponDto) {
        Validator.checkNotNull("weapon.id", weaponDto.getId());
        Validator.checkNull("weapon.instructorId", weaponDto.getInstructorId());
        Validator.checkSize("weapon.type", weaponDto.getType(), 1, 100);
        Validator.checkSize("weapon.model", weaponDto.getModel(), 1, 100);
        Validator.checkNull("weapon.number", weaponDto.getNumber());
    }

    @Override
    public WeaponDto merge(Long id, WeaponDto weaponDto) {
        Validator.checkNotNull("id", id);
        validation(weaponDto);

        return null;
    }
}
