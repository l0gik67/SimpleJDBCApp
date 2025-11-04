package by.l0gik67.jdbc.utils;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JspHelper {
    private static final String JSP_PATH = "/WEB-INF/jsp/";

    public static String jspPath(String path) {
        return JSP_PATH + path + ".jsp";
    }
}
