package ru.cft.focusstart.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.cft.focusstart.api.dto.VisitDto;
import ru.cft.focusstart.service.visit.VisitService;

import java.util.List;

@RestController
@RequestMapping(path = Paths.VISITS, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @GetMapping
    public List<VisitDto> get(
            @RequestParam(name = Parameters.VISIT_DATE_TIME_FROM, required = false) String dateTimeFrom,
            @RequestParam(name = Parameters.VISIT_DATE_TIME_TO, required = false) String dateTimeTo,
            @RequestParam(name = Parameters.VISIT_FULL_NAME_CLIENT, required = false) String fullNameClient
    ) {
        return visitService.get(dateTimeFrom, dateTimeTo, fullNameClient);
    }

    @GetMapping(path = Paths.ID)
    public VisitDto getById(@PathVariable(name = Parameters.ID) Long id) {
        return visitService.getById(id);
    }

    @PostMapping
    public VisitDto create(@RequestBody VisitDto visitDto) {
        return visitService.create(visitDto);
    }

    @PutMapping(path = Paths.ID)
    public VisitDto merge(@PathVariable(name = Parameters.ID) Long id, @RequestBody VisitDto visitDto) {
        return visitService.merge(id, visitDto);
    }

    @DeleteMapping(path = Paths.ID)
    public void delete(@PathVariable(name = Parameters.ID) Long id) {
        visitService.delete(id);
    }
}
