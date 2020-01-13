package ru.cft.focusstart.mapper;

import ru.cft.focusstart.api.dto.WeaponDto;
import ru.cft.focusstart.entity.Weapon;

public class WeaponMapper {

    private static final WeaponMapper INSTANCE = new WeaponMapper();

    private WeaponMapper() {}

    public static WeaponMapper getInstance() {
        return INSTANCE;
    }

    public WeaponDto toDto(Weapon weapon) {
        return WeaponDto.builder()
                .setId(weapon.getId())
                .setInstructorId(weapon.getInstructor().getId())
                .setType(weapon.getType())
                .setModel(weapon.getModel())
                .setSeries(weapon.getSeries())
                .setNumber(weapon.getNumber())
                .build();
    }
}
