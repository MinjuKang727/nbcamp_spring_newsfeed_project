package com.sparta.newsfeed_project.domain.user.dto.response;

import com.sparta.newsfeed_project.domain.user.entity.User;
import lombok.Getter;

@Getter
public class SignupResponseDto {
    private final Long myId;
    private final String username;
    private final String email;
    private final String password;

    public SignupResponseDto(User user) {
        this.myId = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }
}
