package ru.cft.focusstart.service.visit;

import ru.cft.focusstart.api.dto.VisitDto;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitService {

    List<VisitDto> get(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo, String fullNameClient);

    VisitDto getById(Long id);

    VisitDto create(VisitDto visitDto);

    VisitDto merge(Long id, VisitDto instructorDto);

    void delete(Long id);
}
