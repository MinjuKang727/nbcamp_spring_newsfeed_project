package com.sparta.newsfeed_project.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {

    @Size(min = 8, message = "비밀번호는 최소 8글자 이상입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$", message = "비밀번호는 대소문자 포함 영문 + 숫자 + 특수문자 최소 1글자씩 포함합니다.")
    private String password;
    private String newUsername;
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String newEmail;
    @Size(min = 8, message = "비밀번호는 최소 8글자 이상입니다.")
    private String newPassword;
    @Pattern(regexp = "^(01[016789]{1})-?[0-9]{3,4}-?[0-9]{4}$", message = "알 수 없는 형식의 휴대폰 번호입니다.")
    private String newPhoneNumber;
    private String newImageSrc;

    public UserUpdateRequestDto(String password, String newUsername, String newEmail, String newPassword, String newPhoneNumber, String newImageSrc) {
        this.password = password;
        this.newUsername = newUsername;
        this.newEmail = newEmail;
        this.newPassword = newPassword;
        this.newPhoneNumber = newPhoneNumber;
        this.newImageSrc = newImageSrc;
    }
}
