package com.sparta.newsfeed_project.domain.user.service;

import com.sparta.newsfeed_project.auth.jwt.JwtUtil;
import com.sparta.newsfeed_project.domain.common.exception.CommonException;
import com.sparta.newsfeed_project.domain.common.exception.ExceptionCode;
import com.sparta.newsfeed_project.domain.user.dto.request.UserDeleteRequestDto;
import com.sparta.newsfeed_project.domain.user.dto.request.UserCreateRequestDto;
import com.sparta.newsfeed_project.domain.user.dto.request.UserUpdateRequestDto;
import com.sparta.newsfeed_project.domain.user.dto.response.UserReadResponseDto;
import com.sparta.newsfeed_project.domain.user.dto.response.UserCUResponseDto;
import com.sparta.newsfeed_project.domain.user.entity.User;
import com.sparta.newsfeed_project.domain.user.entity.UserRole;
import com.sparta.newsfeed_project.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
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
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public UserCUResponseDto signup(UserCreateRequestDto requestDto) throws CommonException {
        log.trace("signup() 메서드 실행");
        String email = requestDto.getEmail();
        String password = this.passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        if (this.userRepository.countAllByEmail(email) > 0) {
            throw new CommonException(ExceptionCode.FAILED_SIGNUP, new CommonException(ExceptionCode.ALREADY_EXIST_EMAIL));
        }

        // 휴대폰 번호 중복확인
        String phoneNumber = requestDto.getPhoneNumber();
        if (userRepository.countAllByPhoneNumberAndIsDeleted(phoneNumber, 0) > 0) {
            throw new CommonException(ExceptionCode.FAILED_SIGNUP, new CommonException(ExceptionCode.ALREADY_EXIST_PHONE_NUMBER));
        }

        // 사용자 ROLE 확인
        UserRole role = UserRole.USER;
        if(requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new CommonException(ExceptionCode.FAILED_SIGNUP, new CommonException(ExceptionCode.INVALID_ADMIN_TOKEN));
            }
            role = UserRole.ADMIN;
        }

        // 사용자 등록
        User user = new User(requestDto, password, role);
        User savedUser = this.userRepository.save(user);

        return new UserCUResponseDto(savedUser);
    }

    /**
     * 사용자 조회
     * @param userId : 사용자 고유 식별자
     * @return 사용자명, 이메일, 이미지주소가 담긴 ResponseDto
     * @throws NullPointerException
     */
    public UserReadResponseDto getUser(long userId) throws CommonException {
        log.trace("getUser() 메서드 실행");
        User user = this.userRepository.findUserByIdAndIsDeleted(userId, 0)
                .orElseThrow(() ->
                    new CommonException(ExceptionCode.FAILED_VIEW_USER, new CommonException(ExceptionCode.USER_NOT_FOUND))
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
    public UserCUResponseDto updateUser(User user, UserUpdateRequestDto requestDto) throws CommonException {
        log.trace("updateUser() 메서드 실행");
        String email = requestDto.getNewEmail();
        String password = this.passwordEncoder.encode(requestDto.getPassword());
        if (!this.passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new CommonException(ExceptionCode.FAILED_UPDATE_USER, new CommonException(ExceptionCode.INCORRECT_PASSWORD));
        } else if (email != null) {
            if (!Objects.equals(user.getEmail(), email) && this.userRepository.countAllByEmail(email) > 0) {
                throw new CommonException(ExceptionCode.FAILED_UPDATE_USER, new CommonException(ExceptionCode.ALREADY_EXIST_EMAIL));
            }

            user.updateUser(requestDto, password);
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
    public void deleteUser(User user, UserDeleteRequestDto requestDto) throws CommonException {
        log.trace("deleteUser() 메서드 실행");
        if (!Objects.equals(user.getEmail(), requestDto.getEmail())) {
            throw new CommonException(ExceptionCode.FAILED_DELETE_USER, new CommonException(ExceptionCode.NOT_MY_ACCOUNT));
        }

        if (!this.passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new CommonException(ExceptionCode.FAILED_DELETE_USER, new CommonException(ExceptionCode.INCORRECT_PASSWORD));
        }

        user.delete();
        this.userRepository.saveAndFlush(user);
    }

    public String createToken(UserCUResponseDto responseDto) throws UnsupportedEncodingException {
        log.trace("createToken() 메서드 실행");
        return jwtUtil.createToken(responseDto.getId(), responseDto.getEmail(), responseDto.getUsername(), responseDto.getRole());
    }

}
