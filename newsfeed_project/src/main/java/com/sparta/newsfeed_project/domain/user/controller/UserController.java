package com.sparta.newsfeed_project.domain.user.controller;

import com.sparta.newsfeed_project.auth.jwt.JwtUtil;
import com.sparta.newsfeed_project.auth.security.UserDetailsImpl;
import com.sparta.newsfeed_project.domain.common.exception.CommonException;
import com.sparta.newsfeed_project.domain.common.exception.ExceptionCode;
import com.sparta.newsfeed_project.domain.token.TokenBlacklistService;
import com.sparta.newsfeed_project.domain.user.dto.request.UserCreateRequestDto;
import com.sparta.newsfeed_project.domain.user.dto.request.UserDeleteRequestDto;
import com.sparta.newsfeed_project.domain.user.dto.request.UserUpdateRequestDto;
import com.sparta.newsfeed_project.domain.user.dto.response.UserCUResponseDto;
import com.sparta.newsfeed_project.domain.user.dto.response.UserReadResponseDto;
import com.sparta.newsfeed_project.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Slf4j(topic = "UserController")
@Validated
@RestController
public class UserController {
    private final UserService userService;
    private final TokenBlacklistService tokenBlacklistService;

    public UserController(UserService userService, TokenBlacklistService tokenBlacklistService) {
        this.userService = userService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    /**
     * 회원 가입
     * @param requestDto : 회원 가입 정보를 담은 객체
     * @return 회원 가입 결과
     */
    @PostMapping("/users/signup")
    public ResponseEntity<UserCUResponseDto> signup(@RequestBody @Valid UserCreateRequestDto requestDto) throws CommonException, ConstraintViolationException {
        log.info(":::회원 가입:::");
        try {
            UserCUResponseDto responseDto = this.userService.signup(requestDto);
            String bearerToken = this.userService.createToken(responseDto);

            return ResponseEntity.status(HttpStatus.OK).header(JwtUtil.AUTHORIZATION_HEADER, bearerToken).body(responseDto);
        } catch (UnsupportedEncodingException e) {
            log.error("토큰 생성 에러 : {}", e.getMessage());

            throw new CommonException(ExceptionCode.FAILED_CREATE_TOKEN, e);
        }
    }

    /**
     * 프로필 조회
     * @param userId : 조회할 사용자 고유 번호
     * @return 조회된 유저 객체를 담은 ResponseEntity
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserReadResponseDto> getUser(@PathVariable long userId) throws CommonException {
        log.info(":::프로필 조회 (user id : {}):::", userId);

        UserReadResponseDto responseDto = this.userService.getUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    /**
     * 본인의 사용자 정보 수정
     * @param requestDto : 수정할 사용자 정보를 담은 객체
     * @return 수정된 유저 사용자 객체를 담은 ResponseEntity
     */
    @PutMapping("/users")
    public ResponseEntity<UserCUResponseDto> updateUser(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid UserUpdateRequestDto requestDto) throws CommonException, IOException {
        log.info(":::회원 정보 수정:::");
        if (!this.tokenBlacklistService.isTokenBlackListed(request)) {
            try {
                UserCUResponseDto responseDto = this.userService.updateUser(userDetails.getUser(), requestDto);
                String bearerToken = this.userService.createToken(responseDto);

                return ResponseEntity.status(HttpStatus.OK).header(JwtUtil.AUTHORIZATION_HEADER, bearerToken).body(responseDto);
            } catch (UnsupportedEncodingException e) {
                log.error("토큰 생성 에러 : {}", e.getMessage());
                throw new CommonException(ExceptionCode.FAILED_UPDATE_USER, new CommonException(ExceptionCode.FAILED_CREATE_TOKEN));
            }
        }

        throw new CommonException(ExceptionCode.FAILED_UPDATE_USER, new CommonException(ExceptionCode.EXPIRED_JWT_TOKEN));
    }

    /**
     * 회원 탈퇴
     * @param userDetails : 인증 정보를 담은 객체
     * @param requestDto : 탈퇴할 계정 이메일과 비밀번호를 담은 객체
     * @return : 회원 탈퇴 결과
     */
    @DeleteMapping("/users")
    public ResponseEntity<String> deleteUser(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid UserDeleteRequestDto requestDto) throws CommonException, IOException {
        log.info(":::회원 탈퇴:::");

        if (!this.tokenBlacklistService.isTokenBlackListed(request)) {
            this.userService.deleteUser(userDetails.getUser(), requestDto);

            return ResponseEntity.status(HttpStatus.OK).body("회원 탈퇴 성공");
        }

        throw new CommonException(ExceptionCode.FAILED_UPDATE_USER, new CommonException(ExceptionCode.EXPIRED_JWT_TOKEN));
    }


}
