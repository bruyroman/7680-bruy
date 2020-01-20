package ru.cft.focusstart.service.visit;

import ru.cft.focusstart.api.dto.VisitDto;

import java.util.List;

public interface VisitService {

    List<VisitDto> get(String dateTimeFrom, String dateTimeTo, String fullNameClient);

    VisitDto getById(Long id);

    VisitDto create(VisitDto visitDto);

    VisitDto merge(Long id, VisitDto instructorDto);

    void delete(Long id);
}
