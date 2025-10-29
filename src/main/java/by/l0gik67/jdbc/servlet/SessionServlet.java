package by.l0gik67.jdbc.servlet;

import by.l0gik67.jdbc.dto.UserDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebServlet("/session")
public class SessionServlet extends HttpServlet {
    private final static String USER = "user";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var session = req.getSession();
        var user = session.getAttribute(USER);
        if (user == null) {
            user = UserDto.builder()
                    .id(12L)
                    .email("l0gi@mail.ru")
                    .build();
        }

        session.setAttribute(USER, user);
    }
}
