package ru.cft.focusstart.mapper;

import org.springframework.stereotype.Component;
import ru.cft.focusstart.api.dto.WeaponDto;
import ru.cft.focusstart.entity.Weapon;

@Component
public class WeaponMapper {

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
