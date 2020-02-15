package ru.cft.focusstart.repository.visit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.cft.focusstart.entity.Visit;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    String GET_BY_FULLNAME_AND_DATETIME_START_AND_END_QUERY =
            "select v from Visit v join fetch v.client " +
                    "where lower(concat(v.client.surname, ' ', v.client.name, ' ', coalesce(v.client.patronymic, ''))) like lower(concat('%', :fullNameClient, '%')) " +
                    " and (cast(:dateTimeFrom as date) is null or :dateTimeFrom < v.datetimeStart or :dateTimeFrom < v.datetimeEnd)" +
                    " and (cast(:dateTimeTo as date) is null or :dateTimeTo > v.datetimeStart or :dateTimeTo > v.datetimeEnd)";

    @Query(value = GET_BY_FULLNAME_AND_DATETIME_START_AND_END_QUERY)
    List<Visit> find(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo, String fullNameClient);
}
