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
                .setId(visit.getId())
                .setSurname(visit.getClient().getSurname())
                .setName(visit.getClient().getName())
                .setPatronymic(visit.getClient().getPatronymic())
                .setBirthdate(visit.getClient().getBirthdate())
                .setInstructorId(visit.getInstructor().getId())
                .setWeaponId(visit.getWeapon().getId())
                .setDatetimeStart(visit.getDatetimeStart())
                .setDatetimeEnd(visit.getDatetimeEnd())
                .build();
    }
}
