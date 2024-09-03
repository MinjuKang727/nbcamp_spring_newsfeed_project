package com.sparta.newsfeed_project.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.convert.ReadingConverter;

import java.time.LocalDateTime;

//게시글 전체 조회 반환값
@Getter
@AllArgsConstructor
public class PostSimpleResponseDto {
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

}
