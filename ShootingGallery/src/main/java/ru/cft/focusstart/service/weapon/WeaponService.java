package ru.cft.focusstart.service.weapon;

import ru.cft.focusstart.api.dto.WeaponDto;

import java.util.List;

public interface WeaponService {

    List<WeaponDto> get(String type, String model, String fullNameInstructor);

    WeaponDto getById(Long id);

    WeaponDto create(WeaponDto weaponDto);

    WeaponDto merge(Long id, WeaponDto instructorDto);
}
