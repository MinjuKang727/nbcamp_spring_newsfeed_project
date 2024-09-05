package com.sparta.newsfeed_project.auth.jwt;

import com.sparta.newsfeed_project.domain.common.exception.CommonException;
import com.sparta.newsfeed_project.domain.common.exception.ExceptionCode;
import com.sparta.newsfeed_project.domain.user.entity.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PERFIX = "Bearer ";
    public static final String AUTHENTICATION_ERROR = "Authentication_error";
    public static final String AUTHORIZATION_ERROR = "Authorization_error";
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분
    private final HttpServletResponse httpServletResponse;

    @Value("${jwt.secret.key}")
    private String secretkey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    public JwtUtil(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    @PostConstruct
    public void intit() {
        byte[] bytes = Base64.getDecoder().decode(secretkey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    /**
     * JWT 생성
     * @param name : 사용자명
     * @param email : 사용자 이메일
     * @return JWT 토큰
     */
    public String createToken(Long myId, String email, String name, UserRole role) throws UnsupportedEncodingException {
        Date date = new Date();

        String token = Jwts.builder()
                                .setSubject(email)
                                .claim("myName", name)
                                .claim("myId", myId)
                                .claim(AUTHORIZATION_KEY, role)
                                .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                                .setIssuedAt(date)
                                .signWith(key, signatureAlgorithm)
                                .compact();

        return URLEncoder.encode(token, "UTF-8").replaceAll("\\+", "%20");
    }

    /**
     * JWT 검증
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

            return true;
        } catch ( SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT signature, 유효하지 않은 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }


    /**
     * JWT에서 사용자 정보 가져오기
     */
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }


    public String getTokentFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (bearerToken != null) {
            try {
                return URLDecoder.decode(bearerToken, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * JWT 토큰의 앞 BEARER_PREFIX 자르기
     * @param tokenValue : 토큰 값
     * @return BEARER_PREFIX를 자른 토큰 값
     */
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PERFIX)) {
            return tokenValue.substring(7);
        }

        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }


}
