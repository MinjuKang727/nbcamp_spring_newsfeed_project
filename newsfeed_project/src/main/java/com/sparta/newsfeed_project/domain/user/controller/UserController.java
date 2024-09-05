package com.sparta.newsfeed_project.domain.user.controller;

import com.sparta.newsfeed_project.auth.security.UserDetailsImpl;
import com.sparta.newsfeed_project.domain.user.dto.request.UserDeleteRequestDto;
import com.sparta.newsfeed_project.domain.user.dto.request.UserCreateRequestDto;
import com.sparta.newsfeed_project.domain.user.dto.request.UserUpdateRequestDto;
import com.sparta.newsfeed_project.domain.user.dto.response.UserReadResponseDto;
import com.sparta.newsfeed_project.domain.user.dto.response.UserCUResponseDto;
import com.sparta.newsfeed_project.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 회원 가입
     * @param requestDto : 회원 가입 정보를 담은 객체
     * @param bindingResult : Validation 에러를 담은 객체
     * @return 회원 가입 결과
     */
    @PostMapping("/users/signup")
    public ResponseEntity<Object> signup(@RequestBody @Valid UserCreateRequestDto requestDto, BindingResult bindingResult) {
        log.info(":::UserController - 회원 가입:::");
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0) {
            Map<String, String> errorMap = Map.of("message", "Validation 에러");


            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());

                log.error("[Validation 에러] {} 필드 : {}", fieldError.getField(), fieldError.getDefaultMessage());
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
        } else {
            try {
                UserCUResponseDto responseDto = this.userService.signup(requestDto);
                String bearerToken = this.userService.createToken(responseDto);

                return ResponseEntity
                        .ok()
                        .header(HttpHeaders.AUTHORIZATION, bearerToken)
                        .body(responseDto);
            } catch (UnsupportedEncodingException e) {
                log.error("토큰 생성 에러 : {}", e.getMessage());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "토큰 생성 에러 : " + e.getMessage()));
            } catch (IllegalArgumentException e) {
                log.error("회원가입 실패 : {}", e.getMessage());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "회원가입 실패 : " + e.getMessage()));
            }
        }
    }

    /**
     * 프로필 조회
     * @param userId : 조회할 사용자 고유 번호
     * @return 조회된 유저 객체를 담은 ResponseEntity
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable long userId) {
        log.info(":::UserController - 프로필 조회 (user id : {}):::", userId);

        try {
            UserReadResponseDto responseDto = this.userService.getUser(userId);
            return ResponseEntity.ok(responseDto);
        } catch (NullPointerException e) {
            log.error("사용자 조회 실패 : {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "사용자 조회 실패 : " + e.getMessage()));
        }
    }


    /**
     * 본인의 사용자 정보 수정
     * @param requestDto : 수정할 사용자 정보를 담은 객체
     * @return 수정된 유저 사용자 객체를 담은 ResponseEntity
     */
    @PutMapping("/users")
    public ResponseEntity<Object> updateUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid UserUpdateRequestDto requestDto) {
        log.info(":::UserController - 회원 정보 수정:::");
        try {
            UserCUResponseDto responseDto = this.userService.updateUser(userDetails.getUser(), requestDto);
            String bearerToken = this.userService.createToken(responseDto);

            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.AUTHORIZATION, bearerToken)
                    .body(responseDto);
        } catch (UnsupportedEncodingException e) {
            log.error("토큰 생성 에러 : {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "토큰 생성 에러 : " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            log.error("사용자 정보 수정 실패 : {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "사용자 정보 수정 실패 : " + e.getMessage()));
        }
    }

    /**
     * 회원 탈퇴
     * @param userDetails : 인증 정보를 담은 객체
     * @param requestDto : 탈퇴할 계정 이메일과 비밀번호를 담은 객체
     * @return : 회원 탈퇴 결과
     */
    @DeleteMapping("/users")
    public ResponseEntity<Object> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid UserDeleteRequestDto requestDto) {
        log.info(":::UserController - 회원 탈퇴:::");
        try {
            this.userService.deleteUser(userDetails.getUser(), requestDto);

            return ResponseEntity.ok().body(Map.of("message", "회원 탈퇴 완료"));

        } catch (IllegalArgumentException e) {
            log.error("회원 탈퇴 실패 : {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "회원 탈퇴 실패 : " + e.getMessage()));
        }
    }


}
