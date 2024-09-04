package com.sparta.newsfeed_project.domain.comment.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class CommentSaveResponseDto {

    private final String content;
    private final PostDto post;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public CommentSaveResponseDto(String content, PostDto post, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.content = content;
        this.post = post;
       this.createdAt = createdAt;
       this.modifiedAt = modifiedAt;
    }

}
