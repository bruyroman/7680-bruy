package ru.cft.focusstart.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.cft.focusstart.api.dto.WeaponDto;
import ru.cft.focusstart.service.weapon.WeaponService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/weapons/*")
public class WeaponServlet extends HttpServlet {

    private static final String WEAPONS_PATTERN = "^/weapons";
    private static final String WEAPON_PATTERN = "^/weapons/(?<id>[0-9]+)$";

    private final ObjectMapper mapper = new ObjectMapper();
    private final WeaponService weaponService = null;
    private final ExceptionHandler exceptionHandler = ExceptionHandler.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = getPath(req);
            if (path.matches(WEAPONS_PATTERN)) {
                get(req, resp);
            } else if (path.matches(WEAPON_PATTERN)) {
                getById(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            exceptionHandler.handleExceptions(e, resp);
        }
    }

    private void get(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String type = req.getParameter("type");
        String model = req.getParameter("model");
        String fullNameInstructor = req.getParameter("fullNameInstructor");

        List<WeaponDto> response = weaponService.get(type, model, fullNameInstructor);
        writeResp(resp, response);
    }

    private void getById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = PathParser.getPathPart(getPath(req), WEAPON_PATTERN, "id");

        WeaponDto response = weaponService.getById(id);
        writeResp(resp, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = getPath(req);
            if (path.matches(WEAPONS_PATTERN)) {
                create(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            exceptionHandler.handleExceptions(e, resp);
        }
    }

    private void create(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WeaponDto request = mapper.readValue(req.getInputStream(), WeaponDto.class);

        WeaponDto response = weaponService.create(request);
        writeResp(resp, response);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = getPath(req);
            if (path.matches(WEAPON_PATTERN)) {
                merge(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            exceptionHandler.handleExceptions(e, resp);
        }
    }

    private void merge(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = PathParser.getPathPart(getPath(req), WEAPON_PATTERN, "id");
        WeaponDto request = mapper.readValue(req.getInputStream(), WeaponDto.class);

        WeaponDto response = weaponService.merge(id, request);
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
