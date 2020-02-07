package ru.cft.focusstart.repository.weapon;

import org.springframework.stereotype.Repository;
import ru.cft.focusstart.entity.Weapon;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaWeaponRepository implements WeaponRepository {

    private static final String GET_BY_FULLNAME_INSTRUCTOR_AND_TYPE_AND_MODEL_QUERY =
            "select w from Weapon w join fetch w.instructor i join fetch i.person p " +
                    "where lower(w.type) like lower(concat('%', :type, '%')) and lower(w.model) like lower(concat('%', :model, '%')) " +
                    " and lower(concat(w.instructor.person.surname, ' ', w.instructor.person.name, ' ', coalesce(w.instructor.person.patronymic, ''))) like lower(concat('%', :fullNameInstructor, '%'))";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Weapon> get(String type, String model, String fullNameInstructor) {
        return entityManager.createQuery(GET_BY_FULLNAME_INSTRUCTOR_AND_TYPE_AND_MODEL_QUERY, Weapon.class)
                .setParameter("type", type == null ? "" : type)
                .setParameter("model", model == null ? "" : model)
                .setParameter("fullNameInstructor", fullNameInstructor == null ? "" : fullNameInstructor)
                .getResultList();
    }

    @Override
    public Optional<Weapon> getById(Long id) {
        return Optional.ofNullable(entityManager.find(Weapon.class, id));
    }

    @Override
    public void add(Weapon weapon) {
        entityManager.persist(weapon);
    }
}