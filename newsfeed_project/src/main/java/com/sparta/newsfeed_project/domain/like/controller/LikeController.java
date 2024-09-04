package com.sparta.newsfeed_project.domain.like.controller;

import com.sparta.newsfeed_project.auth.security.UserDetailsImpl;
import com.sparta.newsfeed_project.domain.like.dto.LikeCommentRequestDto;
import com.sparta.newsfeed_project.domain.like.dto.LikeCommentResponseDto;
import com.sparta.newsfeed_project.domain.like.dto.LikePostRequestDto;
import com.sparta.newsfeed_project.domain.like.dto.LikePostResponseDto;
import com.sparta.newsfeed_project.domain.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/like-post")
    @ResponseStatus(HttpStatus.OK)
    public void likePost(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody LikePostRequestDto likePostRequestDto) {
        likeService.likePost(userDetails.getUser(), likePostRequestDto.getPostId());
    }

    @GetMapping("/like-post")
    public ResponseEntity<List<LikePostResponseDto>> getLikePost(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(likeService.getLikePost(userDetails.getUser()));
    }

    @PostMapping("/like-comment")
    @ResponseStatus(HttpStatus.OK)
    public void likeComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody LikeCommentRequestDto likeCommentRequestDto) {
        likeService.likeComment(userDetails.getUser(), likeCommentRequestDto.getCommentId());
    }

    @GetMapping("/like-comment")
    public ResponseEntity<List<LikeCommentResponseDto>> getLikeComment(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(likeService.getLikeComment(userDetails.getUser()));
    }

    @DeleteMapping("/unlike-post")
    @ResponseStatus(HttpStatus.OK)
    public void unlikePost(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody LikePostRequestDto likePostRequestDto) {
        likeService.unlikePost(userDetails.getUser(), likePostRequestDto.getPostId());
    }

    @DeleteMapping("/unlike-comment")
    @ResponseStatus(HttpStatus.OK)
    public void unlikeComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody LikeCommentRequestDto likeCommentRequestDto) {
        likeService.unlikeComment(userDetails.getUser(), likeCommentRequestDto.getCommentId());
    }
}
