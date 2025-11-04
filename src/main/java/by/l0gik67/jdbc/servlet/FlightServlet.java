package by.l0gik67.jdbc.servlet;


import by.l0gik67.jdbc.service.FlightService;
import by.l0gik67.jdbc.utils.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/flights")
public class FlightServlet extends HttpServlet {
    private static final FlightService flightService = FlightService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setAttribute("flights", flightService.findAll());
        req.getRequestDispatcher(JspHelper.jspPath("flight")).forward(req, resp);
    }
}
