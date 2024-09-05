package com.sparta.newsfeed_project.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentSaveResponseDto {
    private final PostDto postDto;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
}
