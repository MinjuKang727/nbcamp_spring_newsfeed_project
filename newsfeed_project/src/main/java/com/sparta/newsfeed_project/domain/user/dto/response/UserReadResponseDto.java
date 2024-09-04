package com.sparta.newsfeed_project.domain.user.dto.response;

import jakarta.persistence.GeneratedValue;
import lombok.Getter;

@Getter
public class UserReadResponseDto {
    private final String username;
    private final String email;
    private final String imageSrc;

    public UserReadResponseDto(String username, String email, String imageSrc) {
        this.username = username;
        this.email = email;
        this.imageSrc = imageSrc;
    }
}
