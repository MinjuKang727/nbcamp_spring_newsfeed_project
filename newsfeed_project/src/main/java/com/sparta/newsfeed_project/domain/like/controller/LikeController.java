package com.sparta.newsfeed_project.domain.like.controller;

import com.sparta.newsfeed_project.domain.like.dto.LikeCommentRequestDto;
import com.sparta.newsfeed_project.domain.like.dto.LikePostRequestDto;
import com.sparta.newsfeed_project.domain.like.dto.LikeRequestDto;
import com.sparta.newsfeed_project.domain.like.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/like-post")
    @ResponseStatus(HttpStatus.OK)
    public void likePost(HttpServletRequest httpServletRequest, @RequestBody LikePostRequestDto likePostRequestDto) {
        likeService.likePost((Long) httpServletRequest.getAttribute("myId"), likePostRequestDto.getPostId());
    }

    @PostMapping("/like-comment")
    @ResponseStatus(HttpStatus.OK)
    public void likeComment(HttpServletRequest httpServletRequest, @RequestBody LikeCommentRequestDto likeCommentRequestDto) {
        likeService.likeComment((Long) httpServletRequest.getAttribute("myId"), likeCommentRequestDto.getCommentId());
    }

    @DeleteMapping("/unlike-post")
    @ResponseStatus(HttpStatus.OK)
    public void unlikePost(HttpServletRequest httpServletRequest, @RequestBody LikePostRequestDto likePostRequestDto) {
        likeService.unlikePost((Long) httpServletRequest.getAttribute("myId"), likePostRequestDto.getPostId());
    }

    @DeleteMapping("/unlike-comment")
    @ResponseStatus(HttpStatus.OK)
    public void unlikeComment(HttpServletRequest httpServletRequest, @RequestBody LikeCommentRequestDto likeCommentRequestDto) {
        likeService.unlikeComment((Long) httpServletRequest.getAttribute("myId"), likeCommentRequestDto.getCommentId());
    }
}
