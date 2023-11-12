package org.goit.handlers.cookiehandler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Optional;

public class CookieHandler {
    Cookie [] cookies;
    HttpServletResponse response;
    public CookieHandler(HttpServletRequest request, HttpServletResponse response){
       cookies = request.getCookies();
       this.response = response;
    }

    public Optional<Cookie> getNeededCookie(String cookieName){
        return Arrays.stream(cookies)
                    .filter(c -> c.getName().equals(cookieName) && !c.getValue().trim().isEmpty())
                    .findFirst();
    }

    public void setLastTimezoneCookie(String timezone){
        Cookie cookie = new Cookie("lastTimezone", timezone);
        response.addCookie(cookie);
    }

}
