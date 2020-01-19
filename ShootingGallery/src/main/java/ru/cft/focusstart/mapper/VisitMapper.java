package ru.cft.focusstart.mapper;

import ru.cft.focusstart.api.dto.VisitDto;
import ru.cft.focusstart.entity.Visit;

public class VisitMapper {

    private static final VisitMapper INSTANCE = new VisitMapper();

    private VisitMapper() {}

    public static VisitMapper getInstance() {
        return INSTANCE;
    }

    public VisitDto toDto(Visit visit) {
        return VisitDto.builder()
                .id(visit.getId())
                .surname(visit.getClient().getSurname())
                .name(visit.getClient().getName())
                .patronymic(visit.getClient().getPatronymic())
                .birthdate(visit.getClient().getBirthdate())
                .instructorId(visit.getInstructor().getId())
                .weaponId(visit.getWeapon().getId())
                .datetimeStart(visit.getDatetimeStart())
                .datetimeEnd(visit.getDatetimeEnd())
                .build();
    }
}
