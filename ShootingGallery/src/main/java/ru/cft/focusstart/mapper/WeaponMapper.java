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
                .id(weapon.getId())
                .instructorId(weapon.getInstructor().getId())
                .type(weapon.getType())
                .model(weapon.getModel())
                .series(weapon.getSeries())
                .number(weapon.getNumber())
                .build();
    }
}
