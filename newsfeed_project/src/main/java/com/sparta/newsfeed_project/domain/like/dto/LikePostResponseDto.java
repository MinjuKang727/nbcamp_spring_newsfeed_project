package com.sparta.newsfeed_project.domain.like.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LikePostResponseDto {
    private final Long userId;
    private final String userEmail;
    private final String username;
    private final Long postId;
    private final String postTitle;
    private final String postContent;
}
