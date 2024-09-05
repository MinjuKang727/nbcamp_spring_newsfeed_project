package com.sparta.newsfeed_project.domain.user.controller;

import com.sparta.newsfeed_project.auth.security.UserDetailsImpl;
import com.sparta.newsfeed_project.domain.common.ApiResponse;
import com.sparta.newsfeed_project.domain.common.exception.CommonException;
import com.sparta.newsfeed_project.domain.common.exception.ExceptionCode;
import com.sparta.newsfeed_project.domain.user.dto.request.UserCreateRequestDto;
import com.sparta.newsfeed_project.domain.user.dto.request.UserDeleteRequestDto;
import com.sparta.newsfeed_project.domain.user.dto.request.UserUpdateRequestDto;
import com.sparta.newsfeed_project.domain.user.dto.response.UserCUResponseDto;
import com.sparta.newsfeed_project.domain.user.dto.response.UserReadResponseDto;
import com.sparta.newsfeed_project.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@Slf4j(topic = "UserController")
@Validated
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 회원 가입
     * @param requestDto : 회원 가입 정보를 담은 객체
     * @return 회원 가입 결과
     */
    @PostMapping("/users/signup")
    public ApiResponse<UserCUResponseDto> signup(@RequestBody @Valid UserCreateRequestDto requestDto, HttpServletResponse response) throws CommonException, ConstraintViolationException {
        log.info(":::회원 가입:::");
        try {
            UserCUResponseDto responseDto = this.userService.signup(requestDto);
            String bearerToken = this.userService.createToken(responseDto);
            response.addHeader(HttpHeaders.AUTHORIZATION, bearerToken);

            return ApiResponse.createSuccess("회원 가입 성공", HttpStatus.OK.value(), responseDto);
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
    public ApiResponse<UserReadResponseDto> getUser(@PathVariable long userId) throws CommonException {
        log.info(":::프로필 조회 (user id : {}):::", userId);

        UserReadResponseDto responseDto = this.userService.getUser(userId);
        return ApiResponse.createSuccess("프로필 조회 성공", HttpStatus.OK.value(), responseDto);
    }


    /**
     * 본인의 사용자 정보 수정
     * @param requestDto : 수정할 사용자 정보를 담은 객체
     * @return 수정된 유저 사용자 객체를 담은 ResponseEntity
     */
    @PutMapping("/users")
    public ApiResponse<UserCUResponseDto> updateUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid UserUpdateRequestDto requestDto, HttpServletResponse response) throws CommonException {
        log.info(":::회원 정보 수정:::");
        try {
            UserCUResponseDto responseDto = this.userService.updateUser(userDetails.getUser(), requestDto);
            String bearerToken = this.userService.createToken(responseDto);
            response.addHeader(HttpHeaders.AUTHORIZATION, bearerToken);

            return ApiResponse.createSuccess("회원 정보 수정 성공", HttpStatus.OK.value(), responseDto);
        } catch (UnsupportedEncodingException e) {
            log.error("토큰 생성 에러 : {}", e.getMessage());
            throw new CommonException(ExceptionCode.FAILED_UPDATE_USER, new CommonException(ExceptionCode.FAILED_CREATE_TOKEN));
        }
    }

    /**
     * 회원 탈퇴
     * @param userDetails : 인증 정보를 담은 객체
     * @param requestDto : 탈퇴할 계정 이메일과 비밀번호를 담은 객체
     * @return : 회원 탈퇴 결과
     */
    @DeleteMapping("/users")
    public ApiResponse<Void> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid UserDeleteRequestDto requestDto) throws CommonException {
        log.info(":::회원 탈퇴:::");
        this.userService.deleteUser(userDetails.getUser(), requestDto);

        return ApiResponse.createSuccess("회원 탈퇴 성공", HttpStatus.OK.value(), null);
    }


}
