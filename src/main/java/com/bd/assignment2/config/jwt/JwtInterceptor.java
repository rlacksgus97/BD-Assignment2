package com.bd.assignment2.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("X-AUTH-TOKEN");
        if (token != null && token.length() > 0) {
            return jwtService.isValid(token);
        } else {
            throw new RuntimeException("유효한 인증 토큰이 존재하지 않습니다.");
        }
    }
}
