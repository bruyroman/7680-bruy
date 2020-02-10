package ru.cft.focusstart.repository.weapon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.cft.focusstart.entity.Weapon;

import java.util.List;

public interface WeaponRepository extends JpaRepository<Weapon, Long> {

    String GET_BY_FULLNAME_INSTRUCTOR_AND_TYPE_AND_MODEL_QUERY =
            "select w from Weapon w join fetch w.instructor i join fetch i.person p " +
                    "where lower(w.type) like lower(concat('%', :type, '%')) and lower(w.model) like lower(concat('%', :model, '%')) " +
                    " and lower(concat(w.instructor.person.surname, ' ', w.instructor.person.name, ' ', coalesce(w.instructor.person.patronymic, ''))) like lower(concat('%', :fullNameInstructor, '%'))";

    @Query(GET_BY_FULLNAME_INSTRUCTOR_AND_TYPE_AND_MODEL_QUERY)
    List<Weapon> find(String type, String model, String fullNameInstructor);
}
