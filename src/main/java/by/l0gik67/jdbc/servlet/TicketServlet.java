package by.l0gik67.jdbc.servlet;

import by.l0gik67.jdbc.service.TicketService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/tickets")
public class TicketServlet extends HttpServlet {
    private static final TicketService TICKET_SERVICE = TicketService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        var flightId = Long.valueOf(req.getParameter("flightId"));
        try (var writer = resp.getWriter()) {
            writer.print("<h1> Билеты на полет %s </h1>".formatted(flightId));
            writer.print("<ul>");
            TICKET_SERVICE.findAllByFlightId(flightId).stream().forEach(ticket -> {
                writer.print("""
                             <li>
                             %s
                             </li>
                             """.formatted(ticket.seatNumber()));
            });
            writer.print("</ul>");
        }


    }
}
