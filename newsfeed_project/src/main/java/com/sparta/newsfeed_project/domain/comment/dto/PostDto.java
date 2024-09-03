package com.sparta.newsfeed_project.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

//포스트 Dto entity 를 Dto에 넣지 않기 위함
@Getter
@AllArgsConstructor
public class PostDto {
    private Long postId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;



}
