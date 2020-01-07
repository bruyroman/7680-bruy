package ru.cft.focusstart.api;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/instructors/*")
public class InstructorServlet extends HttpServlet {

    private static final String INSTRUCTORS_PATTERN = "^/instructors$";
    private static final String INSTRUCTOR_PATTERN = "^/instructors/(?<id>[0-9]+)$";
    private static final String INSTRUCTOR_WEAPONS_PATTERN = "^/instructors/(?<id>[0-9]+)/weapons$";
    private static final String INSTRUCTOR_VISITS_PATTERN = "^/instructors/(?<id>[0-9]+)/visits$";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {

    }

}
