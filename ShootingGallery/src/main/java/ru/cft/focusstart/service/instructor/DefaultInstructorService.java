package ru.cft.focusstart.service.instructor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cft.focusstart.api.dto.InstructorDto;
import ru.cft.focusstart.api.dto.VisitDto;
import ru.cft.focusstart.api.dto.WeaponDto;
import ru.cft.focusstart.entity.Instructor;
import ru.cft.focusstart.entity.Person;
import ru.cft.focusstart.entity.types.InstructorCategory;
import ru.cft.focusstart.exception.ObjectNotFoundException;
import ru.cft.focusstart.mapper.InstructorMapper;
import ru.cft.focusstart.mapper.VisitMapper;
import ru.cft.focusstart.mapper.WeaponMapper;
import ru.cft.focusstart.repository.instructor.InstructorRepository;
import ru.cft.focusstart.service.validation.Validator;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultInstructorService implements InstructorService {

    private final InstructorRepository instructorRepository;
    private final InstructorMapper instructorMapper;
    private final WeaponMapper weaponMapper;
    private final VisitMapper visitMapper;

    @Override
    @Transactional(readOnly = true)
    public List<InstructorDto> get(String fullName, String category) {
        InstructorCategory instructorCategory = null;
        if (category != null && category.length() > 0) {
            Validator.checkCategory("category", category);
            instructorCategory = InstructorCategory.valueOf(category);
        }
        return instructorRepository.get(fullName, instructorCategory)
                .stream()
                .map(instructorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public InstructorDto getById(Long id) {
        Validator.checkNotNull("id", id);

        Instructor instructor = getInstructor(id);

        return instructorMapper.toDto(instructor);
    }

    private Instructor getInstructor(Long id) {
        return instructorRepository.getById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Instructor with id %s not found", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<WeaponDto> getWeapons(Long id) {
        Validator.checkNotNull("id", id);

        return Optional.ofNullable(getInstructor(id).getWeapons())
                .orElse(Collections.emptyList())
                .stream()
                .map(weaponMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VisitDto> getVisits(Long id) {
        Validator.checkNotNull("id", id);

        return Optional.ofNullable(getInstructor(id).getVisits())
                .orElse(Collections.emptyList())
                .stream()
                .map(visitMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InstructorDto create(InstructorDto instructorDto) {
        validate(instructorDto);

        Instructor instructor = add(null, instructorDto);

        return instructorMapper.toDto(instructor);
    }

    private void validate(InstructorDto instructorDto) {
        Validator.checkNull("instructor.id", instructorDto.getId());
        Validator.checkSize("instructor.surname", instructorDto.getSurname(), 1, 100);
        Validator.checkSize("instructor.name", instructorDto.getName(), 1, 100);
        Validator.checkNotNull("instructor.birthdate", instructorDto.getBirthdate());
        Validator.checkCategory("instructor.category", instructorDto.getCategory());
    }

    private Instructor add(Long id, InstructorDto instructorDto) {
        Instructor instructor = new Instructor();
        instructor.setId(id);
        instructor.setPerson(toPerson(instructorDto));
        instructor.setCategory(InstructorCategory.valueOf(instructorDto.getCategory()));

        instructorRepository.add(instructor);

        return instructor;
    }

    private Person toPerson(InstructorDto instructorDto) {
        return updatePerson(new Person(), instructorDto);
    }

    private Person updatePerson(Person person, InstructorDto instructorDto) {
        person.setSurname(instructorDto.getSurname());
        person.setName(instructorDto.getName());
        person.setPatronymic(instructorDto.getPatronymic());
        person.setBirthdate(instructorDto.getBirthdate());
        return person;
    }

    @Override
    @Transactional
    public InstructorDto merge(Long id, InstructorDto instructorDto) {
        Validator.checkNotNull("id", id);
        validate(instructorDto);

        Instructor instructor = instructorRepository.getById(id)
                .map(existing -> update(existing, instructorDto))
                .orElseGet(() -> add(id, instructorDto));

        return instructorMapper.toDto(instructor);
    }

    private Instructor update(Instructor instructor, InstructorDto instructorDto) {
        updatePerson(instructor.getPerson(), instructorDto);
        instructor.setCategory(InstructorCategory.valueOf(instructorDto.getCategory()));

        return instructor;
    }
}
