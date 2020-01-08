package ru.cft.focusstart.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/instructors/*")
public class InstructorServlet extends HttpServlet {

    private static final String INSTRUCTORS_PATTERN = "^/instructors$";
    private static final String INSTRUCTOR_PATTERN = "^/instructors/(?<id>[0-9]+)$";
    private static final String INSTRUCTOR_WEAPONS_PATTERN = "^/instructors/(?<id>[0-9]+)/weapons$";
    private static final String INSTRUCTOR_VISITS_PATTERN = "^/instructors/(?<id>[0-9]+)/visits$";

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
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
            //TODO:ECXEPTION
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

    private void getWeapons(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //TODO:LOGIC
        writeResp(resp, "getWeapons");
    }

    private void getVisits(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //TODO:LOGIC
        writeResp(resp, "getVisits");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String path = getPath(req);
            if (path.matches(INSTRUCTORS_PATTERN)) {
                create(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            //TODO:ECXEPTION
        }
    }

    private void create(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //TODO:LOGIC
        writeResp(resp, "create");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String path = getPath(req);
            if (path.matches(INSTRUCTOR_PATTERN)) {
                merge(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            //TODO:ECXEPTION
        }
    }

    private void merge(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //TODO:LOGIC
        writeResp(resp, "merge");
    }

    private String getPath(HttpServletRequest req) {
        return req.getRequestURI().substring(req.getContextPath().length());
    }

    private void writeResp(HttpServletResponse resp, Object response) throws IOException {
        resp.setContentType("application/json");
        mapper.writeValue(resp.getOutputStream(), response);
    }
}
