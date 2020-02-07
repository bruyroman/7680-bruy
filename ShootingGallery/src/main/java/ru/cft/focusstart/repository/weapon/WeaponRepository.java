package ru.cft.focusstart.repository.weapon;

import ru.cft.focusstart.entity.Weapon;

import java.util.List;
import java.util.Optional;

public interface WeaponRepository {

    List<Weapon> get(String type, String model, String fullNameInstructor);

    Optional<Weapon> getById(Long id);

    void add(Weapon weapon);
}
