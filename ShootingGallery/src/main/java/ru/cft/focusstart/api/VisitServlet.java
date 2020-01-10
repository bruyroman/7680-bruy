package ru.cft.focusstart.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/visits/*")
public class VisitServlet extends HttpServlet {

    private static final String VISITS_PATTERN = "^/visits";
    private static final String VISIT_PATTERN = "^/visits/(?<id>[0-9]+)$";

    private final ObjectMapper mapper = new ObjectMapper();
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
        String name = req.getParameter("name");
        //TODO:LOGIC
        writeResp(resp, name + "\r\nGET");
    }

    private void getById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //TODO:LOGIC
        writeResp(resp, "getById");
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
        //TODO:LOGIC
        writeResp(resp, "create");
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
        //TODO:LOGIC
        writeResp(resp, "merge");
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
        //TODO:LOGIC
        writeResp(resp, "delete");
    }

    private String getPath(HttpServletRequest req) {
        return req.getRequestURI().substring(req.getContextPath().length());
    }

    private void writeResp(HttpServletResponse resp, Object response) throws IOException {
        resp.setContentType("application/json");
        mapper.writeValue(resp.getOutputStream(), response);
    }
}
