package com.sparta.newsfeed_project.domain.user.service;

import com.sparta.newsfeed_project.auth.jwt.JwtUtil;
import com.sparta.newsfeed_project.domain.common.exception.CommonException;
import com.sparta.newsfeed_project.domain.common.exception.ExceptionCode;
import com.sparta.newsfeed_project.domain.user.UserException;
import com.sparta.newsfeed_project.domain.user.dto.request.UserCreateRequestDto;
import com.sparta.newsfeed_project.domain.user.dto.request.UserDeleteRequestDto;
import com.sparta.newsfeed_project.domain.user.dto.request.UserUpdateRequestDto;
import com.sparta.newsfeed_project.domain.user.dto.response.UserCUResponseDto;
import com.sparta.newsfeed_project.domain.user.dto.response.UserReadResponseDto;
import com.sparta.newsfeed_project.domain.user.entity.User;
import com.sparta.newsfeed_project.domain.user.entity.UserRole;
import com.sparta.newsfeed_project.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.NoSuchElementException;
import java.util.Objects;

@Slf4j(topic = "UserService")
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // ADMIN_TOKEN
    @Value("${jwt.admin.token}")
    private String ADMIN_TOKEN;

    @Transactional
    public UserCUResponseDto signup(UserCreateRequestDto requestDto) throws UserException {
        log.trace("signup() 메서드 실행");
        String email = requestDto.getEmail();
        String password = this.passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        if (this.userRepository.existsByEmail(email)) {
            throw new UserException("회원 가입 실패", new IllegalArgumentException("해당 이메일의 사용자가 이미 존재합니다."));
        }

        // 휴대폰 번호 중복확인
        String phoneNumber = requestDto.getPhoneNumber();
        if (this.userRepository.existsByPhoneNumberAndIsDeleted(phoneNumber, 0)) {
            throw new UserException("회원 가입 실패", new IllegalArgumentException("해당 휴대폰 번호의 사용자가 이미 존재합니다."));
        }

        // 사용자 ROLE 확인
        UserRole role = UserRole.USER;
        if(requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new UserException("회원 가입 실패", new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다."));
            }
            role = UserRole.ADMIN;
        }

        // 사용자 등록
        User user = new User(requestDto, password, role);
        User savedUser = this.userRepository.saveAndFlush(user);

        return new UserCUResponseDto(savedUser);
    }

    /**
     * 프로필 조회
     * @param userId : 사용자 고유 식별자
     * @return 사용자명, 이메일, 이미지주소가 담긴 ResponseDto
     * @throws NullPointerException
     */
    public UserReadResponseDto getUser(long userId) throws UserException {
        log.trace("getUser() 메서드 실행");
        User user = this.userRepository.findUserByIdAndIsDeleted(userId, 0)
                .orElseThrow(() ->
                    new UserException("프로필 조회 실패", new NoSuchElementException("해당 사용자를 찾을 수 없습니다."))
                );

        return new UserReadResponseDto(
                user.getUsername(),
                user.getEmail(),
                user.getImageSrc()
        );
    }

    /**
     * 회원 정보 수정
     * @param user : 로그인한 사용자 Entity
     * @param requestDto : 수정할 사용자 정보를 담은 객체
     * @throws IllegalArgumentException : 비밀번호 입력 오류 or 수정한 이메일이 이미 DB에 존재할 때, 발생
     */
    @Transactional
    public UserCUResponseDto updateUser(User user, UserUpdateRequestDto requestDto) throws UserException {
        log.trace("updateUser() 메서드 실행");
        // 비밀번호 확인
        if (!this.passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new UserException("회원 정보 수정 실패", new IllegalArgumentException("비밀번호를 잘못입력하셨습니다."));
        } else if (requestDto.getNewEmail() != null) {  // 이메일 확인
            String email = requestDto.getNewEmail();

            if (!Objects.equals(user.getEmail(), email) && this.userRepository.existsByEmail(email)) {
                throw new UserException("회원 정보 수정 실패", new IllegalArgumentException("해당 이메일의 사용자가 이미 존재합니다."));
            }
        }

        user.update(requestDto);

        if (requestDto.getNewPassword() != null) {
            String password = this.passwordEncoder.encode(requestDto.getNewPassword());
            user.setPassword(password);
        }

        User updatedUser = this.userRepository.saveAndFlush(user);
        return new UserCUResponseDto(updatedUser);
    }

    /**
     * 회원 탈퇴
     * @param user : 현재 로그인 중인 사용자 Entity
     * @param requestDto : 탈퇴 인증을 위한 email과 password가 담긴 requestDto
     * @throws IllegalArgumentException : 현재 로그인 한 계정과 탈퇴 요청한 계정이 다른 경우, 비밀번호가 일치하지 않는 경우 발생
     */
    @Transactional
    public void deleteUser(User user, UserDeleteRequestDto requestDto) throws UserException {
        log.trace("deleteUser() 메서드 실행");
        if (!Objects.equals(user.getEmail(), requestDto.getEmail())) {
            throw new UserException("회원 탈퇴 실패", new IllegalAccessException("회원 탈퇴는 본인 계정만 탈퇴 가능합니다."));
        }

        if (!this.passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new UserException("회원 탈퇴 실패", new IllegalArgumentException("비밀번호를 잘못입력하셨습니다."));
        }

        user.delete();
        this.userRepository.saveAndFlush(user);
    }

    public String createToken(UserCUResponseDto responseDto) throws UnsupportedEncodingException {
        log.trace("createToken() 메서드 실행");
        return jwtUtil.createToken(responseDto.getId(), responseDto.getEmail(), responseDto.getUsername(), responseDto.getRole());
    }

}
