package com.sparta.newsfeed_project.domain.comment.dto;

import com.sparta.newsfeed_project.domain.post.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

//댓글 조회 반환값
@Getter
@AllArgsConstructor
public class CommentSimpleResponseDto {
    private final String Content;
    private final String userName;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    //어떤 게시물에 다는 댓글인지
    private final PostDto post;


}
