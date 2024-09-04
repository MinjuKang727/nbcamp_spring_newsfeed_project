package com.sparta.newsfeed_project.domain.post.dto;

import com.sparta.newsfeed_project.domain.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

//게시글 수정 요청값
@Getter
public class PostUpdateRequestDto {
    private Long postId;
    private User postUser;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
