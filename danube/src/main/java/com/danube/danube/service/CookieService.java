package com.danube.danube.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
    public void setCookie(HttpServletResponse response, String jwtKey){
        Cookie cookie = new Cookie("jwt-key", jwtKey);
        response.addCookie(cookie);
    }
}
