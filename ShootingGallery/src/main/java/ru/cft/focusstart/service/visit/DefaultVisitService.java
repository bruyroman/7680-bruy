package ru.cft.focusstart.service.visit;

import ru.cft.focusstart.api.dto.VisitDto;
import ru.cft.focusstart.entity.Instructor;
import ru.cft.focusstart.entity.Person;
import ru.cft.focusstart.entity.Visit;
import ru.cft.focusstart.entity.Weapon;
import ru.cft.focusstart.exception.ObjectNotFoundException;
import ru.cft.focusstart.mapper.VisitMapper;
import ru.cft.focusstart.repository.instructor.InstructorRepository;
import ru.cft.focusstart.repository.visit.VisitRepository;
import ru.cft.focusstart.repository.weapon.WeaponRepository;
import ru.cft.focusstart.service.validation.Validator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultVisitService implements VisitService {

    private static final DefaultVisitService INSTANCE = new DefaultVisitService();

    private final InstructorRepository instructorRepository = null;
    private final WeaponRepository weaponRepository = null;
    private final VisitRepository visitRepository = null;
    private final VisitMapper visitMapper = VisitMapper.getInstance();

    private DefaultVisitService() {}

    public static VisitService getInstance() {
        return INSTANCE;
    }

    @Override
    public List<VisitDto> get(String dateTimeFrom, String dateTimeTo, String fullNameClient) {
        Validator.checkLocalDateTime("dateTimeFrom", dateTimeFrom);
        Validator.checkLocalDateTime("dateTimeTo", dateTimeTo);
        return visitRepository.get(LocalDateTime.parse(dateTimeFrom), LocalDateTime.parse(dateTimeTo), fullNameClient)
                .stream()
                .map(visitMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public VisitDto getById(Long id) {
        Validator.checkNotNull("id", id);

        Visit visit = getVisit(id);

        return visitMapper.toDto(visit);
    }

    private Visit getVisit(Long id) {
        return visitRepository.getById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Visit with id %s not found", id)));
    }

    @Override
    public VisitDto create(VisitDto visitDto) {
        validate(visitDto);
        Validator.checkNotNull("visit.datetimeStart", visitDto.getDatetimeStart());

        Visit visit = add(null, visitDto);

        return visitMapper.toDto(visit);
    }

    private void validate(VisitDto visitDto) {
        Validator.checkNull("visit.id", visitDto.getId());
        Validator.checkSize("visit.surname", visitDto.getSurname(), 1, 100);
        Validator.checkSize("visit.name", visitDto.getName(), 1, 100);
        Validator.checkNotNull("visit.birthdate", visitDto.getBirthdate());
        Validator.checkNotNull("visit.instructorId", visitDto.getInstructorId());
        Validator.checkNotNull("visit.weaponId", visitDto.getWeaponId());
    }

    private Visit add(Long id, VisitDto visitDto) {
        Visit visit = new Visit();
        visit.setId(id);
        visit.setClient(toClient(visitDto));
        visit.setInstructor(getInstructor(visitDto.getInstructorId()));
        visit.setWeapon(getWeapon(visitDto.getWeaponId()));
        visit.setDatetimeStart(visitDto.getDatetimeStart());
        visit.setDatetimeEnd(visitDto.getDatetimeEnd());

        visitRepository.add(visit);

        return visit;
    }

    private Person toClient(VisitDto visitDto) {
        Person person = new Person();
        person.setSurname(visitDto.getSurname());
        person.setName(visitDto.getName());
        person.setPatronymic(visitDto.getPatronymic());
        person.setBirthdate(visitDto.getBirthdate());
        return person;
    }

    private Instructor getInstructor(Long id) {
        return instructorRepository.getById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Instructor with id %s not found", id)));
    }

    private Weapon getWeapon(Long id) {
        return weaponRepository.getById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Weapon with id %s not found", id)));
    }

    @Override
    public VisitDto merge(Long id, VisitDto visitDto) {
        Validator.checkNotNull("id", id);
        validate(visitDto);

        Visit visit = visitRepository.getById(id)
                .map(existing -> update(existing, visitDto))
                .orElseGet(() -> add(id, visitDto));

        return visitMapper.toDto(visit);
    }

    private Visit update(Visit visit, VisitDto visitDto) {
        visit.setClient(toClient(visitDto));

        if (!visitDto.getInstructorId().equals(visit.getInstructor().getId())) {
            visit.setInstructor(getInstructor(visitDto.getInstructorId()));
        }

        if (!visitDto.getWeaponId().equals(visit.getWeapon().getId())) {
            visit.setWeapon(getWeapon(visitDto.getWeaponId()));
        }

        visit.setDatetimeStart(visitDto.getDatetimeStart());
        visit.setDatetimeEnd(visitDto.getDatetimeEnd());

        visitRepository.update(visit);

        return visit;
    }

    @Override
    public void delete(Long id) {
        Validator.checkNotNull("id", id);

        Visit visit = getVisit(id);

        visitRepository.delete(visit);
    }
}
