package util;

import java.io.IOException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Misc {

    public static String referer(HttpServletRequest request) {
        return "//" + request.getHeader("Referer").split("/")[1] + request.getHeader("Referer").split("/")[2] + "/CEJV__659_frontend";
//      return request.getHeader("Referer").split("/")[0] + "//" + request.getHeader("Referer").split("/")[1] + request.getHeader("Referer").split("/")[2];
    }

    public static void setSystemMessage(HttpServletResponse response, String coockieName, String message) throws IOException {
        Cookie systemMessageCookie = new Cookie(coockieName, message);
        response.addCookie(systemMessageCookie);
    }

}
