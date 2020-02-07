package ru.cft.focusstart.service.visit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultVisitService implements VisitService {

    private final InstructorRepository instructorRepository;
    private final WeaponRepository weaponRepository;
    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;

    @Override
    @Transactional(readOnly = true)
    public List<VisitDto> get(String dateTimeFrom, String dateTimeTo, String fullNameClient) {
        LocalDateTime localDateTimeFrom = parseLocalDateTime("dateTimeFrom", dateTimeFrom);
        LocalDateTime localDateTimeTo = parseLocalDateTime("dateTimeTo", dateTimeTo);

        return visitRepository.get(localDateTimeFrom, localDateTimeTo, fullNameClient)
                .stream()
                .map(visitMapper::toDto)
                .collect(Collectors.toList());
    }

    private LocalDateTime parseLocalDateTime(String paremeterName, String strLocalDateTime) {
        LocalDateTime localDateTime = null;
        if (strLocalDateTime != null && strLocalDateTime.length() > 0) {
            Validator.checkLocalDateTime(paremeterName, strLocalDateTime);
            localDateTime = LocalDateTime.parse(strLocalDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return localDateTime;
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional
    public VisitDto create(VisitDto visitDto) {
        validate(visitDto);

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
        Validator.checkRangeLocalDateTime("visit.datetimeStart", "visit.datetimeEnd",
                visitDto.getDatetimeStart(), visitDto.getDatetimeEnd());
        Validator.checkNotNull("visit.datetimeStart", visitDto.getDatetimeStart());
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
        return updateClient(new Person(), visitDto);
    }

    private Person updateClient(Person client, VisitDto visitDto) {
        client.setSurname(visitDto.getSurname());
        client.setName(visitDto.getName());
        client.setPatronymic(visitDto.getPatronymic());
        client.setBirthdate(visitDto.getBirthdate());
        return client;
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
    @Transactional
    public VisitDto merge(Long id, VisitDto visitDto) {
        Validator.checkNotNull("id", id);
        validate(visitDto);

        Visit visit = visitRepository.getById(id)
                .map(existing -> update(existing, visitDto))
                .orElseGet(() -> add(id, visitDto));

        return visitMapper.toDto(visit);
    }

    private Visit update(Visit visit, VisitDto visitDto) {
        updateClient(visit.getClient(), visitDto);

        if (!visitDto.getInstructorId().equals(visit.getInstructor().getId())) {
            visit.setInstructor(getInstructor(visitDto.getInstructorId()));
        }

        if (!visitDto.getWeaponId().equals(visit.getWeapon().getId())) {
            visit.setWeapon(getWeapon(visitDto.getWeaponId()));
        }

        visit.setDatetimeStart(visitDto.getDatetimeStart());
        visit.setDatetimeEnd(visitDto.getDatetimeEnd());

        return visit;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Validator.checkNotNull("id", id);

        Visit visit = getVisit(id);

        visitRepository.delete(visit);
    }
}
