package com.sparta.newsfeed_project.auth.filter;

import com.sparta.newsfeed_project.auth.jwt.JwtUtil;
import com.sparta.newsfeed_project.domain.common.exception.CommonException;
import com.sparta.newsfeed_project.domain.common.exception.ExceptionCode;
import com.sparta.newsfeed_project.domain.user.entity.User;
import com.sparta.newsfeed_project.domain.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;

@Slf4j(topic = "AuthFilter")
@Order(2)
public class AuthFilter implements Filter {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthFilter(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String url = httpServletRequest.getRequestURI();

        if (StringUtils.hasText(url) && (url.startsWith("/users") || url.startsWith("/css") || url.startsWith("/js"))) {
            log.info("인증 처리를 하지 않는 url : " + url);
            chain.doFilter(request, response);
        } else {
            String tokenValue = jwtUtil.getTokentFromRequest(httpServletRequest);

            if (StringUtils.hasText(tokenValue)) {
                String token = jwtUtil.substringToken(tokenValue);

                if (!jwtUtil.validateToken(token)) {
                    throw new IllegalArgumentException("Token Error");
                }

                Claims info = jwtUtil.getUserInfoFromToken(token);

                User user = this.userRepository.findUserByEmail(info.getSubject()).orElseThrow(() ->
                        new NullPointerException("Not Found User")
                );

                request.setAttribute("user", user);
                chain.doFilter(request, response);
            } else {
                throw new IllegalArgumentException("Not Found Token");
            }
        }
    }
}
