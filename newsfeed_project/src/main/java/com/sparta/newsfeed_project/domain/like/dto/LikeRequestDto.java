package com.sparta.newsfeed_project.domain.like.dto;

import lombok.Getter;

@Getter
public class LikeRequestDto {
    private Long postId;
    private Long commentId;
}
