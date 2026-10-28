package com.pelisMatch.movie_catalog.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioRol") == null) {
            response.sendRedirect("/login");
            return false;
        }

        String rol = session.getAttribute("usuarioRol").toString();
        if (request.getRequestURI().startsWith("/admin") && !"ADMIN".equals(rol)) {
            response.sendRedirect("/");
            return false;
        }
        return true;
    }
}
