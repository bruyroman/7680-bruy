package ru.cft.focusstart.repository.reader;

import ru.cft.focusstart.entity.Instructor;
import ru.cft.focusstart.entity.Weapon;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class WeaponReader {

    private WeaponReader() {}

    public static Weapon readWeapon(ResultSet rs) throws SQLException {
        Weapon weapon = new Weapon();
        weapon.setId(rs.getLong("WEAPON_ID"));

        Instructor instructor = new Instructor();
        instructor.setId(rs.getLong("INSTRUCTOR_ID"));
        weapon.setInstructor(instructor);

        weapon.setType(rs.getString("TYPE"));
        weapon.setModel(rs.getString("MODEL"));
        weapon.setSeries(rs.getString("SERIES"));
        weapon.setNumber(rs.getInt("NUMBER"));
        return weapon;
    }
}
