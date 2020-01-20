package ru.cft.focusstart.repository.reader;

import ru.cft.focusstart.entity.Instructor;
import ru.cft.focusstart.entity.InstructorCategory;
import ru.cft.focusstart.entity.Person;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class InstructorReader {

    private InstructorReader() {}

    public static Instructor readInstructor(ResultSet rs) throws SQLException {
        Instructor instructor = new Instructor();
        instructor.setId(rs.getLong("INSTRUCTOR_ID"));

        Person person = new Person();
        person.setId(rs.getLong("PERSON_ID"));
        person.setSurname(rs.getString("SURNAME"));
        person.setName(rs.getString("NAME"));
        person.setPatronymic(rs.getString("PATRONYMIC"));
        person.setBirthdate(rs.getDate("BIRTHDATE").toLocalDate());
        instructor.setPerson(person);

        instructor.setCategory(InstructorCategory.valueOf(rs.getString("CATEGORY")));
        return instructor;
    }
}
