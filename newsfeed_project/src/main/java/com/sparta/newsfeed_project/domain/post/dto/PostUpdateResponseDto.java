package com.sparta.newsfeed_project.domain.post.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

//게시글 수정 반환값
@Getter
@RequiredArgsConstructor
public class PostUpdateResponseDto {
    private final Long postId;
    private final String title;
    private final String content;
    private final UserDto postUser;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public PostUpdateResponseDto(Long postId, String title, String content, UserDto postUser, LocalDateTime createdAt) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.postUser = postUser;
        this.createdAt = createdAt;
        this.modifiedAt = LocalDateTime.now();
    }


}
