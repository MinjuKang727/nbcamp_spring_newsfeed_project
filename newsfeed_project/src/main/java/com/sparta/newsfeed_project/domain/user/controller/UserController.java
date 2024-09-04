package com.sparta.newsfeed_project.domain.user.controller;

import com.sparta.newsfeed_project.auth.jwt.JwtUtil;
import com.sparta.newsfeed_project.auth.security.UserDetailsImpl;
import com.sparta.newsfeed_project.domain.user.dto.request.UserDeleteRequestDto;
import com.sparta.newsfeed_project.domain.user.dto.request.SignupRequestDto;
import com.sparta.newsfeed_project.domain.user.dto.request.UserUpdateRequestDto;
import com.sparta.newsfeed_project.domain.user.dto.response.SignupResponseDto;
import com.sparta.newsfeed_project.domain.user.dto.response.UserReadResponseDto;
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

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/users/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody @Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
        } else {
            try {
                SignupResponseDto responseDto = this.userService.signup(requestDto);

                try {
                    String bearerToken = this.userService.getToken(responseDto.getMyId(), responseDto.getEmail(), responseDto.getUsername());
                    return ResponseEntity
                            .ok()
                            .header(HttpHeaders.AUTHORIZATION, bearerToken)
                            .body(responseDto);
                } catch (UnsupportedEncodingException e) {
                    log.error("토큰 생성 에러");
                    System.out.println(e.getMessage());
                }
            } catch (IllegalArgumentException e) {
                log.error("회원가입 실패");
                System.out.println(e.getMessage());
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * 프로필 조회
     * @param userId : 조회할 사용자 고유 번호
     * @return 조회된 유저 객체를 담은 ResponseEntity
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserReadResponseDto> getUser(@PathVariable long userId) {
        log.info("UserController - getUser() 메서드 실행");

        try {
            UserReadResponseDto responseDto = this.userService.getUser(userId);
            return ResponseEntity.ok(responseDto);
        } catch (NullPointerException e) {
            log.error("사용자 회원 가입 실패");
            log.error(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


    /**
     * 본인의 사용자 정보 수정
     * @param requestDto : 수정할 사용자 정보를 담은 객체
     * @return 수정된 유저 사용자 객체를 담은 ResponseEntity
     */
    @PutMapping("/users")
    public ResponseEntity<Void> updateUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid UserUpdateRequestDto requestDto) {
        log.info("UserController - updateUser() 메서드 실행");
        try {
            this.userService.updateUser(userDetails.getUser(), requestDto);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid UserDeleteRequestDto requestDto) {
        log.info("UserController - deleteUser() 메서드 실행");
        try {
            this.userService.deleteUser(userDetails.getUser(), requestDto);
            ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            log.error("회원 탈퇴 실패");
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


}
