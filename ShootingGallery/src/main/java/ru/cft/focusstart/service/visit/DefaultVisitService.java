package ru.cft.focusstart.service.visit;

import ru.cft.focusstart.api.dto.VisitDto;
import ru.cft.focusstart.service.validation.Validator;

import java.util.List;

public class DefaultVisitService implements VisitService {

    private static final DefaultVisitService INSTANCE = new DefaultVisitService();

    //TODO: Экземпляры репозиториев

    private DefaultVisitService() {}

    public static VisitService getInstance() {
        return INSTANCE;
    }

    @Override
    public List<VisitDto> get(String dateTimeFrom, String dateTimeTo, String fullNameClient) {
        return null;
    }

    @Override
    public VisitDto getById(Long id) {
        Validator.checkNotNull("id", id);

        return null;
    }

    @Override
    public VisitDto create(VisitDto visitDto) {
        validate(visitDto);
        Validator.checkNotNull("visit.datetimeStart", visitDto.getDatetimeStart());
        return null;
    }

    private void validate(VisitDto visitDto) {
        Validator.checkNull("visit.id", visitDto.getId());
        Validator.checkSize("visit.surname", visitDto.getSurname(), 1, 100);
        Validator.checkSize("visit.name", visitDto.getName(), 1, 100);
        Validator.checkNotNull("visit.birthdate", visitDto.getBirthdate());
        Validator.checkNotNull("visit.instructorId", visitDto.getInstructorId());
        Validator.checkNotNull("visit.weaponId", visitDto.getWeaponId());
    }

    @Override
    public VisitDto merge(Long id, VisitDto visitDto) {
        Validator.checkNotNull("id", id);
        validate(visitDto);

        return null;
    }

    @Override
    public void delete(Long id) {
        Validator.checkNotNull("id", id);
    }
}
