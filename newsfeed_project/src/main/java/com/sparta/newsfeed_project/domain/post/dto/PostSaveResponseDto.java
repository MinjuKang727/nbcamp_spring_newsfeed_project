package com.sparta.newsfeed_project.domain.post.dto;

import lombok.Getter;

import java.time.LocalDateTime;

//게시글 등록 반환값
@Getter
public class PostSaveResponseDto {
    private final UserDto postUser;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public PostSaveResponseDto(UserDto postUser, String title, String content) {
        this.postUser = postUser;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }
}
