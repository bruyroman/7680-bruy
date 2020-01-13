package ru.cft.focusstart.repository.weapon;

import ru.cft.focusstart.entity.Weapon;
import ru.cft.focusstart.repository.DataSourceProvider;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class JdbcWeaponRepository implements WeaponRepository {

    private static final JdbcWeaponRepository INSTANCE = new JdbcWeaponRepository();

    private final DataSource dataSource;

    private JdbcWeaponRepository() {
        this.dataSource = DataSourceProvider.getDataSource();
    }

    public static WeaponRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Weapon> get(String type, String model, String fullNameInstructor) {
        return null;
    }

    @Override
    public Optional<Weapon> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public void add(Weapon weapon) {

    }

    @Override
    public void update(Weapon weapon) {

    }
}
