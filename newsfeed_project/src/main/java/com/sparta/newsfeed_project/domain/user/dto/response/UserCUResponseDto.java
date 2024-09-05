package com.sparta.newsfeed_project.domain.user.dto.response;

import com.sparta.newsfeed_project.domain.user.entity.User;
import com.sparta.newsfeed_project.domain.user.entity.UserRole;
import lombok.Getter;

@Getter
public class UserCUResponseDto {
    private final Long id;
    private final String username;
    private final String email;
    private final String password;
    private final UserRole role;

    public UserCUResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
    }
}
