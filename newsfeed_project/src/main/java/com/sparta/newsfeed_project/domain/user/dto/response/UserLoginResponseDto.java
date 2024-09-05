package com.sparta.newsfeed_project.domain.user.dto.response;

import lombok.Getter;

@Getter
public class UserLoginResponseDto {
    private final String bearerToken;

    public UserLoginResponseDto(String bearerToken) {
        this.bearerToken = bearerToken;
    }
}
