package com.sparta.newsfeed_project.auth;

import com.sparta.newsfeed_project.auth.jwt.JwtUtil;
import com.sparta.newsfeed_project.auth.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Slf4j(topic = "UserLogoutHandler")
public class UserLogoutHandler implements LogoutHandler {
    private JwtUtil jwtUtil;

    public UserLogoutHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("로그아웃 시도");

        String email = ((UserDetailsImpl) authentication.getPrincipal()).getEmail();

        try {
            String expiredToken = jwtUtil.createExpiredToken(email);

            response.setStatus(200);
            response.setHeader(JwtUtil.AUTHORIZATION_HEADER, expiredToken);

            log.info("로그아웃 성공");
        } catch (UnsupportedEncodingException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
    }
}
