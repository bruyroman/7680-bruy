package ru.cft.focusstart.repository.instructor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.cft.focusstart.entity.Instructor;

import java.util.List;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    String GET_INSTRUCTOR_BY_FULLNAME_AND_CATEGORY_QUERY =
            "select i from Instructor i join fetch i.person " +
                    "where lower(concat(i.person.surname, ' ', i.person.name, ' ', coalesce(i.person.patronymic, ''))) like lower(concat('%', :fullName, '%')) " +
                    "and lower(CAST(i.category as text)) like lower(concat('%', :category, '%'))";

    @Query(GET_INSTRUCTOR_BY_FULLNAME_AND_CATEGORY_QUERY)
    List<Instructor> find(String fullName, String category);
}
