package com.sparta.newsfeed_project.domain.user.service;

import com.sparta.newsfeed_project.domain.user.dto.request.SignupRequestDto;
import com.sparta.newsfeed_project.domain.user.dto.request.UserDeleteRequestDto;
import com.sparta.newsfeed_project.domain.user.dto.response.SignupResponseDto;
import com.sparta.newsfeed_project.domain.user.dto.request.UserUpdateRequestDto;
import com.sparta.newsfeed_project.domain.user.dto.response.UserReadResponseDto;
import com.sparta.newsfeed_project.domain.user.entity.User;
import com.sparta.newsfeed_project.auth.jwt.JwtUtil;
import com.sparta.newsfeed_project.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Optional;

@Slf4j
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

    @Transactional
    public SignupResponseDto signup(SignupRequestDto requestDto) {
        log.trace("UserService - signup() 메서드 실행");
        String email = requestDto.getEmail();
        String password = this.passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkEmail = userRepository.findUserByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("해당 이메일의 사용자가 존재합니다.");
        }

        // 휴대폰 번호 중복확인
        String phoneNumber = requestDto.getPhoneNumber();
        Optional<User> checkPhoneNumber = userRepository.findUserByPhoneNumberAndIsDeleted(phoneNumber, 0);
        if (checkPhoneNumber.isPresent()) {
            throw new IllegalArgumentException("해당 휴대폰 번호의 사용자가 존재합니다.");
        }

        // 사용자 등록
        User user = new User(requestDto, password);
        User savedUser = this.userRepository.save(user);

        return new SignupResponseDto(savedUser);
    }

    public UserReadResponseDto getUser(long userId) throws NullPointerException {
        User user = this.userRepository.findUserByIdAndIsDeleted(userId, 0)
                .orElseThrow(() ->
                    new NullPointerException("User Not Found")
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
    public void updateUser(User user, UserUpdateRequestDto requestDto) throws IllegalArgumentException {
        if (!Objects.equals(user.getPassword(), requestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호를 잘못입력하셨습니다.");
        } else if (requestDto.getNewEmail() != null) {
            Optional<User> existUser = this.userRepository.findUserByEmail(requestDto.getNewEmail());
            if (existUser.isPresent()) {
                throw new IllegalArgumentException("이미 가입되어 있는 이메일입니다.");
            }

            user.updateUser(requestDto);
        }

        this.userRepository.save(user);
    }

    public void deleteUser(User user, UserDeleteRequestDto requestDto) throws IllegalArgumentException {
        if (!Objects.equals(user.getEmail(), requestDto.getEmail())) {
            throw new IllegalArgumentException("회원 탈퇴는 본인 계정만 탈퇴 가능합니다.");
        }
        if (!Objects.equals(user.getPassword(), requestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호를 잘못입력하셨습니다.");
        }

        this.userRepository.delete(user);
    }

    public String getToken(Long userId, String email, String name) throws UnsupportedEncodingException {
        return jwtUtil.createToken(userId, email, name);
    }

}
