package ru.cft.focusstart.repository.visit;

import org.springframework.stereotype.Repository;
import ru.cft.focusstart.entity.Visit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaVisitRepository implements VisitRepository {

    private static final String GET_BY_FULLNAME_QUERY =
            "select v from Visit v join fetch v.client " +
                    "where lower(concat(v.client.surname, ' ', v.client.name, ' ', coalesce(v.client.patronymic, ''))) like lower(concat('%', :fullNameClient, '%')) ";

    private static final String DATETIME_START_WHERE =
            " and v.datetimeStart = :dateTimeFrom ";

    private static final String DATETIME_END_WHERE =
            " and v.datetimeEnd = :dateTimeTo ";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Visit> get(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo, String fullNameClient) {
        String query = GET_BY_FULLNAME_QUERY + (dateTimeFrom != null ? DATETIME_START_WHERE : "") + (dateTimeTo != null ? DATETIME_END_WHERE : "");

        TypedQuery typedQuery = entityManager.createQuery(query, Visit.class)
                .setParameter("fullNameClient", fullNameClient == null ? "" : fullNameClient);

        if (dateTimeFrom != null) {
            typedQuery.setParameter("dateTimeFrom", dateTimeFrom);
        }

        if (dateTimeTo != null) {
            typedQuery.setParameter("dateTimeTo", dateTimeTo);
        }

        return typedQuery.getResultList();
    }

    @Override
    public Optional<Visit> getById(Long id) {
        return Optional.ofNullable(entityManager.find(Visit.class, id));
    }

    @Override
    public void add(Visit visit) {
        entityManager.persist(visit);
    }

    @Override
    public void delete(Visit visit) {
        entityManager.remove(visit);
    }
}