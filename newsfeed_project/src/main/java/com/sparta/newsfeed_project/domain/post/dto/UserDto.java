package com.sparta.newsfeed_project.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDto {
    private final Long id;
    private final String email;
    private final String userName;

}
