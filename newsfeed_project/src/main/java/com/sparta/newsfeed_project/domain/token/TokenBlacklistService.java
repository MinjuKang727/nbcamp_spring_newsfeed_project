package com.sparta.newsfeed_project.domain.token;

import com.sparta.newsfeed_project.auth.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;

@Slf4j(topic = "TokenBlacklistService")
@Service
@Transactional(readOnly = true)
public class TokenBlacklistService {
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final JwtUtil jwtUtil;

    public TokenBlacklistService(TokenBlacklistRepository tokenBlacklistRepository, JwtUtil jwtUtil) {
        this.tokenBlacklistRepository = tokenBlacklistRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public void addTokenToBlackList(HttpServletRequest request) throws IOException {
        log.info("토큰 블랙리스트에 추가");

        String token = jwtUtil.getDecodedToken(request);
        if (token != null) {
            Date expirationTime = jwtUtil.getExpirationTime(token);
            Token expiredToken = new Token(token, expirationTime);
            this.tokenBlacklistRepository.save(expiredToken);
        }
    }

    public boolean isTokenBlackListed(HttpServletRequest request) throws IOException {
        log.info("토큰 not 블랙리스트 검증");
        String token = jwtUtil.getDecodedToken(request);
        return this.tokenBlacklistRepository.existsByToken(token);
    }

    @Transactional
    public void removeTokenFromBlackList() {
        log.info("만료된 토큰 블랙리스트 정리");
        this.tokenBlacklistRepository.deleteByExpirationTimeBefore(new Date());
    }
}
