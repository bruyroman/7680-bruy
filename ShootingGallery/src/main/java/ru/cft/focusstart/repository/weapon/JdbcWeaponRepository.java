package ru.cft.focusstart.repository.weapon;

import ru.cft.focusstart.entity.Weapon;
import ru.cft.focusstart.repository.DataAccessException;
import ru.cft.focusstart.repository.DataSourceProvider;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

import static ru.cft.focusstart.repository.reader.WeaponReader.readWeapon;

public class JdbcWeaponRepository implements WeaponRepository {

    private static final String GET_QUERY =
            "SELECT  wpn.\"ID\" WEAPON_ID," +
                    "wpn.\"INSTRUCTOR_ID\"," +
                    "wpn.\"TYPE\"," +
                    "wpn.\"MODEL\"," +
                    "wpn.\"SERIES\"," +
                    "wpn.\"NUMBER\" " +
                    "FROM public.\"WEAPON\" wpn";

    private static final String GET_BY_ID_QUERY =
            GET_QUERY +
                    " WHERE wpn.\"ID\" = ?";

    private static final String GET_BY_FULLNAME_INSTRUCTOR_AND_TYPE_AND_MODEL_QUERY =
            GET_QUERY +
                    " inner join public.\"INSTRUCTOR\" inst on inst.\"ID\" = wpn.\"INSTRUCTOR_ID\" " +
                    " inner join public.\"PERSON\" prs on prs.\"ID\" = inst.\"PERSON_ID\" " +
                    " WHERE lower(wpn.\"TYPE\") like lower('%' || ? || '%') and lower(wpn.\"MODEL\") like lower('%' || ? || '%') " +
                    " and lower(prs.\"SURNAME\"||' '||prs.\"NAME\"||' '||prs.\"PATRONYMIC\") like lower('%' || ? || '%') ";

    private static final String ADD_QUERY =
            "INSERT INTO public.\"WEAPON\" (\"INSTRUCTOR_ID\", \"TYPE\", \"MODEL\", \"SERIES\", \"NUMBER\") " +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_QUERY =
            "UPDATE public.\"WEAPON\" SET \"INSTRUCTOR_ID\"=?, \"TYPE\"=?, \"MODEL\"=?, \"SERIES\"=?, \"NUMBER\"=? " +
                    "WHERE \"ID\"=?";

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
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement psWeapon = con.prepareStatement(GET_BY_FULLNAME_INSTRUCTOR_AND_TYPE_AND_MODEL_QUERY);
        ) {
            psWeapon.setString(1, type == null ? "" : type);
            psWeapon.setString(2, model == null ? "" : model);
            psWeapon.setString(3, fullNameInstructor == null ? "" : fullNameInstructor);

            ResultSet rsWeapon = psWeapon.executeQuery();

            Collection<Weapon> weapons = readWeaponList(rsWeapon);

            return new ArrayList<>(weapons);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    private Collection<Weapon> readWeaponList(ResultSet rsWeapon) throws SQLException {
        Map<Long, Weapon> result = new HashMap<>();

        while (rsWeapon.next()) {
            long id = rsWeapon.getLong("WEAPON_ID");

            Weapon weapon = result.get(id);
            if (weapon == null) {
                weapon = readWeapon(rsWeapon);
                result.put(id, weapon);
            }
        }

        return result.values();
    }

    @Override
    public Optional<Weapon> getById(Long id) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement psWeapon = con.prepareStatement(GET_BY_ID_QUERY);
        ) {
            psWeapon.setLong(1, id);

            ResultSet rsWeapon = psWeapon.executeQuery();

            Collection<Weapon> weapons = readWeaponList(rsWeapon);

            if (weapons.isEmpty()) {
                return Optional.empty();
            } else if (weapons.size() == 1) {
                return Optional.of(weapons.iterator().next());
            } else {
                throw new SQLException("Unexpected result set size");
            }
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void add(Weapon weapon) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement psWeapon = con.prepareStatement(ADD_QUERY, Statement.RETURN_GENERATED_KEYS);
        ) {
            psWeapon.setLong(1, weapon.getInstructor().getId());
            psWeapon.setString(2, weapon.getType());
            psWeapon.setString(3, weapon.getModel());
            psWeapon.setString(4, weapon.getSeries());
            psWeapon.setInt(5, weapon.getNumber());
            psWeapon.executeUpdate();

            ResultSet rsWeapon = psWeapon.getGeneratedKeys();
            Long idWeapon = rsWeapon.next() ? rsWeapon.getLong(1) : null;
            if (idWeapon == null) {
                throw new SQLException("Unexpected error - could not obtain id");
            }
            weapon.setId(idWeapon);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void update(Weapon weapon) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement psWeapon = con.prepareStatement(UPDATE_QUERY);
        ) {
            psWeapon.setLong(1, weapon.getInstructor().getId());
            psWeapon.setString(2, weapon.getType());
            psWeapon.setString(3, weapon.getModel());
            psWeapon.setString(4, weapon.getSeries());
            psWeapon.setInt(5, weapon.getNumber());
            psWeapon.setLong(6, weapon.getId());
            psWeapon.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }
}
