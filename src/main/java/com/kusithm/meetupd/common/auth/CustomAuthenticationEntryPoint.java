package com.kusithm.meetupd.common.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kusithm.meetupd.common.error.ErrorCode;
import com.kusithm.meetupd.common.error.dto.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setStatus(ErrorCode.UNAUTHORIZED.getStatus().value());
        response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.of(ErrorCode.UNAUTHORIZED)));
    }
}
