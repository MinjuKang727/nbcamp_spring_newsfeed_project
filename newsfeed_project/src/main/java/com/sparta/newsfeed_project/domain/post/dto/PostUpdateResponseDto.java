package com.sparta.newsfeed_project.domain.post.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

//게시글 수정 반환값
@Getter
public class PostUpdateResponseDto {
    private final Long postId;
    private final String title;
    private final String content;
    private final UserDto postUser;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public PostUpdateResponseDto(UserDto postUser,
                                 Long postId,
                                 String title,
                                 String content,
                                 LocalDateTime createdAt,
                                 LocalDateTime modifiedAt) {
        this.postUser = postUser;
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt =modifiedAt;

    }


}
