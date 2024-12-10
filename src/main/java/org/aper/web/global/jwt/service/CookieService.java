package org.aper.web.global.jwt.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class CookieService {

    public void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        Cookie cookie = findCookie(request, cookieName);
        if (cookie != null) {
            cookie.setValue("");
            cookie.setPath("/");
            cookie.setSecure(true);
            cookie.setAttribute("SameSite", "None");
            cookie.setMaxAge(0);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        }
    }

    @NotNull
    public Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setSecure(true);
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setDomain("aper.cc");
        cookie.setAttribute("SameSite", "None");
//        cookie.setAttribute("SameSite", "Lax");
        return cookie;
    }

    public void setCookie(HttpServletResponse response, String cookieName, String token) {
        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
        Cookie cookie = createCookie(cookieName, encodedToken);
        response.addCookie(cookie);
    }

    public Cookie findCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public String getCookieValue(HttpServletRequest request, String cookieName) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
