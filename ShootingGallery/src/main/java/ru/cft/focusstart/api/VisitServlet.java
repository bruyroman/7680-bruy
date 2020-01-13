package ru.cft.focusstart.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.cft.focusstart.api.dto.VisitDto;
import ru.cft.focusstart.service.visit.DefaultVisitService;
import ru.cft.focusstart.service.visit.VisitService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/visits/*")
public class VisitServlet extends HttpServlet {

    private static final String VISITS_PATTERN = "^/visits";
    private static final String VISIT_PATTERN = "^/visits/(?<id>[0-9]+)$";

    private final ObjectMapper mapper = new ObjectMapper();
    private final VisitService visitService = DefaultVisitService.getInstance();
    private final ExceptionHandler exceptionHandler = ExceptionHandler.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = getPath(req);
            if (path.matches(VISITS_PATTERN)) {
                get(req, resp);
            } else if (path.matches(VISIT_PATTERN)) {
                getById(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            exceptionHandler.handleExceptions(e, resp);
        }
    }

    private void get(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String dateTimeFrom = req.getParameter("dateTimeFrom");
        String dateTimeTo = req.getParameter("dateTimeTo");
        String fullNameClient = req.getParameter("fullNameClient");

        List<VisitDto> response = visitService.get(dateTimeFrom, dateTimeTo, fullNameClient);
        writeResp(resp, response);
    }

    private void getById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = PathParser.getPathPart(getPath(req), VISIT_PATTERN, "id");

        VisitDto response = visitService.getById(id);
        writeResp(resp, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = getPath(req);
            if (path.matches(VISITS_PATTERN)) {
                create(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            exceptionHandler.handleExceptions(e, resp);
        }
    }

    private void create(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        VisitDto request = mapper.readValue(req.getInputStream(), VisitDto.class);

        VisitDto response = visitService.create(request);
        writeResp(resp, response);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = getPath(req);
            if (path.matches(VISIT_PATTERN)) {
                merge(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            exceptionHandler.handleExceptions(e, resp);
        }
    }

    private void merge(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = PathParser.getPathPart(getPath(req), VISIT_PATTERN, "id");
        VisitDto request = mapper.readValue(req.getInputStream(), VisitDto.class);

        VisitDto response = visitService.merge(id, request);
        writeResp(resp, response);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = getPath(req);
            if (path.matches(VISIT_PATTERN)) {
                delete(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            exceptionHandler.handleExceptions(e, resp);
        }
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = PathParser.getPathPart(getPath(req), VISIT_PATTERN, "id");
        visitService.delete(id);
    }

    private String getPath(HttpServletRequest req) {
        return req.getRequestURI().substring(req.getContextPath().length());
    }

    private void writeResp(HttpServletResponse resp, Object response) throws IOException {
        resp.setContentType("application/json");
        mapper.writeValue(resp.getOutputStream(), response);
    }
}
