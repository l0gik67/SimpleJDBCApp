package by.l0gik67.jdbc.servlet;

import by.l0gik67.jdbc.service.TicketService;
import by.l0gik67.jdbc.utils.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/tickets")
public class TicketServlet extends HttpServlet {
    private static final TicketService ticketService = TicketService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var flightId = Long.valueOf(req.getParameter("flightId"));
        var tickets = ticketService.findAllByFlightId(flightId);

        req.setAttribute("flightId", flightId);
        req.setAttribute("tickets", tickets);
        req.getRequestDispatcher(JspHelper.jspPath("tickets")).forward(req, resp);

    }
}
