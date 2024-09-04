package com.sparta.newsfeed_project.domain.post.dto;


import com.sparta.newsfeed_project.domain.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;


//게시물 등록 요청값
@Getter
public class PostSaveRequestDto {
    //postUser = 게시물을 등록하는 사람
    private User postUser;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    }

