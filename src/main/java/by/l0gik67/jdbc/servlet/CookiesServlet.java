package by.l0gik67.jdbc.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;

@WebServlet("/cookies")
public class CookiesServlet extends HttpServlet {

    private final static String UNIQ_USER = "user_id";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var cookies = req.getCookies();
        if (cookies == null ||
                Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(UNIQ_USER)).findAny().isEmpty()) {
            var cookie = new Cookie(UNIQ_USER, "1");
            cookie.setMaxAge(60 * 60);
            cookie.setPath("/");
            resp.addCookie(cookie);
        }
        for (var cookie : cookies) {
            System.out.println(cookie.getName() + " " + cookie.getValue());
        }
    }
}
