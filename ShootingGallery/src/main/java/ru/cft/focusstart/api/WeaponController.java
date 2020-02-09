package ru.cft.focusstart.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.cft.focusstart.api.dto.WeaponDto;
import ru.cft.focusstart.service.weapon.WeaponService;

import java.util.List;

@RestController
@RequestMapping(path = Paths.WEAPONS, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class WeaponController {

    private final WeaponService weaponService;

    @GetMapping
    public List<WeaponDto> get(
            @RequestParam(name = Parameters.WEAPON_TYPE, required = false) String type,
            @RequestParam(name = Parameters.WEAPON_MODEL, required = false) String model,
            @RequestParam(name = Parameters.WEAPON_FULL_NAME_INSTRUCTOR, required = false) String fullNameInstructor
    ) {
        return weaponService.get(type, model, fullNameInstructor);
    }

    @GetMapping(path = Paths.ID)
    public WeaponDto getById(@PathVariable(name = Parameters.ID) Long id) {
        return weaponService.getById(id);
    }

    @PostMapping
    public WeaponDto create(@RequestBody WeaponDto weaponDto) {
        return weaponService.create(weaponDto);
    }

    @PutMapping(path = Paths.ID)
    public WeaponDto merge(@PathVariable(name = Parameters.ID) Long id, @RequestBody WeaponDto weaponDto) {
        return weaponService.merge(id, weaponDto);
    }
}
