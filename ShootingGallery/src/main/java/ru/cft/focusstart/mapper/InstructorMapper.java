package ru.cft.focusstart.mapper;

import ru.cft.focusstart.api.dto.InstructorDto;
import ru.cft.focusstart.entity.Instructor;

public class InstructorMapper {

    private static final InstructorMapper INSTANCE = new InstructorMapper();

    private InstructorMapper() {}

    public static InstructorMapper getInstance() {
        return INSTANCE;
    }

    public InstructorDto toDto(Instructor instructor) {
        return InstructorDto.builder()
                .id(instructor.getId())
                .surname(instructor.getPerson().getSurname())
                .name(instructor.getPerson().getName())
                .patronymic(instructor.getPerson().getPatronymic())
                .birthdate(instructor.getPerson().getBirthdate())
                .category(instructor.getCategory().getName())
                .build();
    }
}
