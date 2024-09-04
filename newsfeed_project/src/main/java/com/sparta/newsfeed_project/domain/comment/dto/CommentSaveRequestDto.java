package com.sparta.newsfeed_project.domain.comment.dto;


import com.sparta.newsfeed_project.domain.post.entity.Post;
import com.sparta.newsfeed_project.domain.user.entity.User;
import lombok.Getter;

@Getter
public class CommentSaveRequestDto {
    private String Content;
    private User user;
    private Post post;

}
