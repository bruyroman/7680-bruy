package ru.cft.focusstart.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.cft.focusstart.api.dto.InstructorDto;
import ru.cft.focusstart.api.dto.VisitDto;
import ru.cft.focusstart.api.dto.WeaponDto;
import ru.cft.focusstart.service.instructor.DefaultInstructorService;
import ru.cft.focusstart.service.instructor.InstructorService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/instructors/*")
public class InstructorServlet extends HttpServlet {

    private static final String INSTRUCTORS_PATTERN = "^/instructors$";
    private static final String INSTRUCTOR_PATTERN = "^/instructors/(?<id>[0-9]+)$";
    private static final String INSTRUCTOR_WEAPONS_PATTERN = "^/instructors/(?<id>[0-9]+)/weapons$";
    private static final String INSTRUCTOR_VISITS_PATTERN = "^/instructors/(?<id>[0-9]+)/visits$";

    private final ObjectMapper mapper = new ObjectMapper();
    private final InstructorService instructorService = DefaultInstructorService.getInstance();
    private final ExceptionHandler exceptionHandler = ExceptionHandler.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = getPath(req);
            if (path.matches(INSTRUCTORS_PATTERN)) {
                get(req, resp);
            } else if (path.matches(INSTRUCTOR_PATTERN)) {
                getById(req, resp);
            } else if (path.matches(INSTRUCTOR_WEAPONS_PATTERN)) {
                getWeapons(req, resp);
            } else if (path.matches(INSTRUCTOR_VISITS_PATTERN)) {
                getVisits(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            exceptionHandler.handleExceptions(e, resp);
        }
    }

    private void get(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String fullName = req.getParameter("fullName");
        String category = req.getParameter("category");

        List<InstructorDto> response = instructorService.get(fullName, category);
        writeResp(resp, response);
    }

    private void getById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = PathParser.getPathPart(getPath(req), INSTRUCTOR_PATTERN, "id");

        InstructorDto response = instructorService.getById(id);
        writeResp(resp, response);
    }

    private void getWeapons(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = PathParser.getPathPart(getPath(req), INSTRUCTOR_WEAPONS_PATTERN, "id");

        List<WeaponDto> response = instructorService.getWeapons(id);
        writeResp(resp, response);
    }

    private void getVisits(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = PathParser.getPathPart(getPath(req), INSTRUCTOR_VISITS_PATTERN, "id");

        List<VisitDto> response = instructorService.getVisits(id);
        writeResp(resp, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = getPath(req);
            if (path.matches(INSTRUCTORS_PATTERN)) {
                create(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            exceptionHandler.handleExceptions(e, resp);
        }
    }

    private void create(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        InstructorDto request = mapper.readValue(req.getInputStream(), InstructorDto.class);

        InstructorDto response = instructorService.create(request);
        writeResp(resp, response);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = getPath(req);
            if (path.matches(INSTRUCTOR_PATTERN)) {
                merge(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            exceptionHandler.handleExceptions(e, resp);
        }
    }

    private void merge(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = PathParser.getPathPart(getPath(req), INSTRUCTOR_PATTERN, "id");
        InstructorDto request = mapper.readValue(req.getInputStream(), InstructorDto.class);

        InstructorDto response = instructorService.merge(id, request);
        writeResp(resp, response);
    }

    private String getPath(HttpServletRequest req) {
        return req.getRequestURI().substring(req.getContextPath().length());
    }

    private void writeResp(HttpServletResponse resp, Object response) throws IOException {
        resp.setContentType("application/json");
        mapper.writeValue(resp.getOutputStream(), response);
    }
}
