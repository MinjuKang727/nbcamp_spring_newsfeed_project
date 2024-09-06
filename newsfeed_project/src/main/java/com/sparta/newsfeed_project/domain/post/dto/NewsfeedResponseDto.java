package com.sparta.newsfeed_project.domain.post.dto;

import com.sparta.newsfeed_project.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NewsfeedResponseDto {
    private final Long postUserId;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public NewsfeedResponseDto(Post post) {
        this.postUserId =post.getUser().getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }



}
