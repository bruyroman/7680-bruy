package ru.cft.focusstart.repository.visit;

import ru.cft.focusstart.entity.Visit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VisitRepository {

    List<Visit> get(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo, String fullNameClient);

    Optional<Visit> getById(Long id);

    void add(Visit visit);

    void update(Visit visit);

    void delete(Visit visit);
}
