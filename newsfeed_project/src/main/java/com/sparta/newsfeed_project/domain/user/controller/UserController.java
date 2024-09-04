package com.sparta.newsfeed_project.domain.user.controller;

import com.sparta.newsfeed_project.auth.jwt.JwtUtil;
import com.sparta.newsfeed_project.domain.user.dto.request.SignupRequestDto;
import com.sparta.newsfeed_project.domain.user.dto.request.UserUpdateRequestDto;
import com.sparta.newsfeed_project.domain.user.dto.response.SignupResponseDto;
import com.sparta.newsfeed_project.domain.user.dto.response.UserReadResponseDto;
import com.sparta.newsfeed_project.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        SignupResponseDto responseDto;

        try {
            responseDto = this.userService.signup(requestDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String bearerToken;
        try {
            bearerToken = this.userService.getToken(responseDto.getMyId(), responseDto.getEmail(), responseDto.getUsername());
        } catch (UnsupportedEncodingException e) {
            log.error("토큰 생성 에러");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }

        return ResponseEntity
                .ok()
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .body(responseDto);
    }

    /**
     * 프로필 조회
     * @param userId : 조회할 사용자 고유 번호
     * @return 조회된 유저 객체를 담은 ResponseEntity
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserReadResponseDto> getUser(@PathVariable long userId) {
        log.trace("UserController - getUser() 메서드 실행");
        return ResponseEntity.ok(this.userService.getUser(userId));
    }


    /**
     * 본인의 사용자 정보 수정
     * @param requestDto : 수정할 사용자 정보를 담은 객체
     * @return 수정된 유저 사용자 객체를 담은 ResponseEntity
     */
    @PutMapping("/users")
    public ResponseEntity<Void> updateUser(HttpServletRequest request, @RequestBody @Valid UserUpdateRequestDto requestDto) {
        log.info("UserController - updateUser() 메서드 실행");
        log.info(request.getHeader(jwtUtil.AUTHORIZATION_HEADER));
        this.userService.updateUser(requestDto);
        return ResponseEntity.ok().build();
    }


}
