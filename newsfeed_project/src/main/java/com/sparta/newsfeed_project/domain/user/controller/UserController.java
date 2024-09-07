package com.sparta.newsfeed_project.domain.user.controller;

import com.sparta.newsfeed_project.auth.jwt.JwtUtil;
import com.sparta.newsfeed_project.auth.security.UserDetailsImpl;
import com.sparta.newsfeed_project.domain.common.exception.CommonException;
import com.sparta.newsfeed_project.domain.common.exception.ExceptionCode;
import com.sparta.newsfeed_project.domain.token.TokenBlacklistService;
import com.sparta.newsfeed_project.domain.user.UserException;
import com.sparta.newsfeed_project.domain.user.dto.request.UserCreateRequestDto;
import com.sparta.newsfeed_project.domain.user.dto.request.UserDeleteRequestDto;
import com.sparta.newsfeed_project.domain.user.dto.request.UserUpdateRequestDto;
import com.sparta.newsfeed_project.domain.user.dto.response.UserCUResponseDto;
import com.sparta.newsfeed_project.domain.user.dto.response.UserReadResponseDto;
import com.sparta.newsfeed_project.domain.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<UserCUResponseDto> signup(HttpServletRequest request, @RequestBody @Valid UserCreateRequestDto requestDto) throws UserException {
        log.info(":::회원 가입:::");
        try {
            UserCUResponseDto responseDto = this.userService.signup(requestDto);
            String bearerToken = this.userService.createToken(responseDto);

            this.tokenBlacklistService.addTokenToBlackList(request);

            return ResponseEntity.status(HttpStatus.OK).header(JwtUtil.AUTHORIZATION_HEADER, bearerToken).body(responseDto);
        } catch (UnsupportedEncodingException | ServletException e) {
            log.error("토큰 생성 에러 : {}", e.getMessage());

            throw new UserException("토큰 생성 실패", e);
        }
    }

    /**
     * 프로필 조회
     * @param userId : 조회할 사용자 고유 번호
     * @return 조회된 유저 객체를 담은 ResponseEntity
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserReadResponseDto> getUser(@PathVariable long userId) throws UserException {
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
    public ResponseEntity<UserCUResponseDto> updateUser(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid UserUpdateRequestDto requestDto) throws UserException {
        log.info(":::회원 정보 수정:::");
        try {
            UserCUResponseDto responseDto = this.userService.updateUser(userDetails.getUser(), requestDto);
            String bearerToken = this.userService.createToken(responseDto);

            this.tokenBlacklistService.addTokenToBlackList(request);

            return ResponseEntity.status(HttpStatus.OK).header(JwtUtil.AUTHORIZATION_HEADER, bearerToken).body(responseDto);
        } catch (UnsupportedEncodingException | UserException | ServletException e) {
            log.error("토큰 생성 에러 : {}", e.getMessage());
            throw new UserException("사용자 정보 수정 실패", e);
        }
    }

    /**
     * 회원 탈퇴
     * @param userDetails : 인증 정보를 담은 객체
     * @param requestDto : 탈퇴할 계정 이메일과 비밀번호를 담은 객체
     * @return : 회원 탈퇴 결과
     */
    @DeleteMapping("/users")
    public ResponseEntity<String> deleteUser(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid UserDeleteRequestDto requestDto) throws UserException, ServletException {
        log.info(":::회원 탈퇴:::");
        this.userService.deleteUser(userDetails.getUser(), requestDto);

        this.tokenBlacklistService.addTokenToBlackList(request);

        return ResponseEntity.status(HttpStatus.OK).body("회원 탈퇴 성공");
    }
}
