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
                .setId(instructor.getId())
                .setSurname(instructor.getPerson().getSurname())
                .setName(instructor.getPerson().getName())
                .setPatronymic(instructor.getPerson().getPatronymic())
                .setBirthdate(instructor.getPerson().getBirthdate())
                .setCategory(instructor.getCategory())
                .build();
    }
}
