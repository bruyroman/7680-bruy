package ru.cft.focusstart.service.instructor;

import ru.cft.focusstart.api.dto.InstructorDto;
import ru.cft.focusstart.api.dto.VisitDto;
import ru.cft.focusstart.api.dto.WeaponDto;

import java.util.List;

public interface InstructorService {

    List<InstructorDto> get(String fullName, String category);

    InstructorDto getById(Long id);

    List<WeaponDto> getWeapons(Long id);

    List<VisitDto> getVisits(Long id);

    InstructorDto create(InstructorDto instructorDto);

    InstructorDto merge(Long id, InstructorDto instructorDto);
}
