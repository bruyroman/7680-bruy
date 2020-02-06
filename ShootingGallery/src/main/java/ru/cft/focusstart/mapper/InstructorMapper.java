package ru.cft.focusstart.mapper;

import org.springframework.stereotype.Component;
import ru.cft.focusstart.api.dto.InstructorDto;
import ru.cft.focusstart.entity.Instructor;

@Component
public class InstructorMapper {

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
