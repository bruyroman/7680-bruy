package ru.cft.focusstart.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.cft.focusstart.api.dto.InstructorDto;
import ru.cft.focusstart.api.dto.VisitDto;
import ru.cft.focusstart.api.dto.WeaponDto;
import ru.cft.focusstart.service.instructor.InstructorService;

import java.util.List;

@RestController
@RequestMapping(path = Paths.INSTRUCTORS, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorService instructorService;

    @GetMapping
    public List<InstructorDto> get(
            @RequestParam(name = Parameters.INSTRUCTOR_FULL_NAME, required = false) String fullName,
            @RequestParam(name = Parameters.INSTRUCTOR_CATEGORY, required = false) String category
    ) {
        return instructorService.get(fullName, category);
    }

    @GetMapping(path = Paths.ID)
    public InstructorDto getById(@PathVariable(name = Parameters.ID) Long id) {
        return instructorService.getById(id);
    }

    @GetMapping(path = Paths.ID + Paths.WEAPONS)
    public List<WeaponDto> getWeapons(@PathVariable(name = Parameters.ID) Long id) {
        return instructorService.getWeapons(id);
    }

    @GetMapping(path = Paths.ID + Paths.VISITS)
    public List<VisitDto> getVisits(@PathVariable(name = Parameters.ID) Long id) {
        return instructorService.getVisits(id);
    }

    @PostMapping
    public InstructorDto create(@RequestBody InstructorDto instructorDto) {
        return instructorService.create(instructorDto);
    }

    @PutMapping(path = Paths.ID)
    public InstructorDto merge(@PathVariable(name = Parameters.ID) Long id, @RequestBody InstructorDto instructorDto) {
        return instructorService.merge(id, instructorDto);
    }
}
