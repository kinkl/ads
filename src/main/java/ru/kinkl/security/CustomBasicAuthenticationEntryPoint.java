package ru.kinkl.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String xrequestedWith = request.getHeader("x-requested-with");
        if (xrequestedWith != null && xrequestedWith.equals("XMLHttpRequest") && authException != null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else{
            super.commence(request, response, authException);
        }
    }
}
