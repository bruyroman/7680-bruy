package ru.cft.focusstart.service.instructor;

import ru.cft.focusstart.api.dto.InstructorDto;
import ru.cft.focusstart.api.dto.VisitDto;
import ru.cft.focusstart.api.dto.WeaponDto;
import ru.cft.focusstart.service.validation.Validator;

import java.util.List;

public class DefaultInstructorService implements InstructorService {

    private static final DefaultInstructorService INSTANCE = new DefaultInstructorService();

    //TODO: Экземпляры репозиториев

    private DefaultInstructorService() {}

    public static InstructorService getInstance() {
        return INSTANCE;
    }

    @Override
    public List<InstructorDto> get(String fullName, String category) {
        return null;
    }

    @Override
    public InstructorDto getById(Long id) {
        Validator.checkNotNull("id", id);

        return null;
    }

    @Override
    public List<WeaponDto> getWeapons(Long id) {
        Validator.checkNotNull("id", id);

        return null;
    }

    @Override
    public List<VisitDto> getVisits(Long id) {
        Validator.checkNotNull("id", id);

        return null;
    }

    @Override
    public InstructorDto create(InstructorDto instructorDto) {
        validate(instructorDto);

        return null;
    }

    private void validate(InstructorDto instructorDto) {
        Validator.checkNull("instructor.id", instructorDto.getId());
        Validator.checkSize("instructor.surname", instructorDto.getSurname(), 1, 100);
        Validator.checkSize("instructor.name", instructorDto.getName(), 1, 100);
        Validator.checkNotNull("instructor.birthdate", instructorDto.getBirthdate());
        Validator.checkNotNull("instructor.category", instructorDto.getCategory());
    }

    @Override
    public InstructorDto merge(Long id, InstructorDto instructorDto) {
        Validator.checkNotNull("id", id);
        validate(instructorDto);

        return null;
    }
}
