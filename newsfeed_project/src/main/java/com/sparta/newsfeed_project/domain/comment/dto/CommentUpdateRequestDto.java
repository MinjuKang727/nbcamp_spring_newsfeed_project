package com.sparta.newsfeed_project.domain.comment.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentUpdateRequestDto {
    private Long userId;
    private Long postId;
    private Long commentId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
