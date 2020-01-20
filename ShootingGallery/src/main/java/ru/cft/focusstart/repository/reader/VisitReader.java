package ru.cft.focusstart.repository.reader;

import ru.cft.focusstart.entity.Instructor;
import ru.cft.focusstart.entity.Person;
import ru.cft.focusstart.entity.Visit;
import ru.cft.focusstart.entity.Weapon;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class VisitReader {

    private VisitReader() {}

    public static Visit readVisit(ResultSet rs) throws SQLException {
        Visit visit = new Visit();
        visit.setId(rs.getLong("VISIT_ID"));

        Person client = new Person();
        client.setId(rs.getLong("CLIENT_ID"));
        client.setSurname(rs.getString("SURNAME"));
        client.setName(rs.getString("NAME"));
        client.setPatronymic(rs.getString("PATRONYMIC"));
        client.setBirthdate(rs.getDate("BIRTHDATE").toLocalDate());
        visit.setClient(client);

        Instructor instructor = new Instructor();
        instructor.setId(rs.getLong("INSTRUCTOR_ID"));
        visit.setInstructor(instructor);

        Weapon weapon = new Weapon();
        weapon.setId(rs.getLong("WEAPON_ID"));
        visit.setWeapon(weapon);

        visit.setDatetimeStart(rs.getTimestamp("DATETIME_START").toLocalDateTime());
        visit.setDatetimeEnd(rs.getTimestamp("DATETIME_END").toLocalDateTime());
        return visit;
    }
}