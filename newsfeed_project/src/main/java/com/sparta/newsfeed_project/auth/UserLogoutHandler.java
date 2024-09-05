package com.sparta.newsfeed_project.auth;

import com.sparta.newsfeed_project.domain.token.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Slf4j(topic = "UserLogoutHandler")
public class UserLogoutHandler implements LogoutHandler {

    private final TokenBlacklistService tokenBlacklistService;

    public UserLogoutHandler(TokenBlacklistService tokenBlacklistService) {
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("로그아웃 시도");
        this.tokenBlacklistService.addTokenToBlackList(request);

    }
}
