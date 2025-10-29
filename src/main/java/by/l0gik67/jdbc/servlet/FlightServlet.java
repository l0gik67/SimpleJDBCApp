package by.l0gik67.jdbc.servlet;


import by.l0gik67.jdbc.service.FlightService;
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

        resp.setContentType("text/html");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try (var writer = resp.getWriter()) {
            writer.print("<h1>Список перелетов</h1>");
            writer.print("<ul>");
            flightService.findAll().stream().forEach(flightDto -> {
                writer.print("""
                            <li>
                            <a href='/tickets?flightId=%d'>%s</a>
                            </li>
                            """.formatted(flightDto.id(), flightDto.description()));

            });

            writer.print("</ul>");
        }

    }
}
