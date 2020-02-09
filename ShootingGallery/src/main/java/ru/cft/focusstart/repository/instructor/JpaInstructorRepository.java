package ru.cft.focusstart.repository.instructor;

import org.springframework.stereotype.Repository;
import ru.cft.focusstart.entity.Instructor;
import ru.cft.focusstart.entity.types.InstructorCategory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaInstructorRepository implements InstructorRepository {

    private static final String GET_INSTRUCTOR_BY_FULLNAME_AND_CATEGORY_QUERY =
           "select i from Instructor i join fetch i.person " +
                   "where lower(concat(i.person.surname, ' ', i.person.name, ' ', coalesce(i.person.patronymic, ''))) like lower(concat('%', :fullName, '%')) " +
                    "and lower(CAST(i.category as text)) like lower(concat('%', :category, '%'))";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Instructor> get(String fullName, InstructorCategory category) {
        return entityManager.createQuery(GET_INSTRUCTOR_BY_FULLNAME_AND_CATEGORY_QUERY, Instructor.class)
                .setParameter("fullName", fullName == null ? "" : fullName)
                .setParameter("category", category == null ? "" : category.getName())
                .getResultList();
    }

    @Override
    public Optional<Instructor> getById(Long id) {
        return Optional.ofNullable(entityManager.find(Instructor.class, id));
    }

    @Override
    public void add(Instructor instructor) {
        entityManager.persist(instructor);
    }
}