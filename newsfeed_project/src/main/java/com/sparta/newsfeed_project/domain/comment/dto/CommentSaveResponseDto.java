package com.sparta.newsfeed_project.domain.comment.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CommentSaveResponseDto {

    private final String content;
    private final PostDto post;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public CommentSaveResponseDto(String content, PostDto post) {
        this.content = content;
        this.post = post;
        createdAt = LocalDateTime.now();
        modifiedAt = LocalDateTime.now();
    }

}
