package ru.cft.focusstart.api;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/weapons/*")
public class WeaponServlet extends HttpServlet {

    private static final String WEAPONS_PATTERN = "^/weapons";
    private static final String WEAPON_PATTERN = "^/weapons/(?<id>[0-9]+)$";

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
