package com.sparta.newsfeed_project.domain.friend.dto;

import lombok.Getter;

@Getter
public class FollowerResponseDto {
    private final String username;
    private final String email;
    private final boolean allowed;

    public FollowerResponseDto(String username, String email, boolean allowed) {
        this.username = username;
        this.email = email;
        this.allowed = allowed;
    }
}
