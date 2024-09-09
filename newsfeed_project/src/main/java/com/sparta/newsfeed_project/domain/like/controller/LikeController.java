package com.sparta.newsfeed_project.domain.like.controller;

import com.sparta.newsfeed_project.auth.security.UserDetailsImpl;
import com.sparta.newsfeed_project.domain.like.dto.LikeCommentRequestDto;
import com.sparta.newsfeed_project.domain.like.dto.LikeCommentResponseDto;
import com.sparta.newsfeed_project.domain.like.dto.LikePostRequestDto;
import com.sparta.newsfeed_project.domain.like.dto.LikePostResponseDto;
import com.sparta.newsfeed_project.domain.like.service.LikeService;
import com.sparta.newsfeed_project.domain.token.TokenBlacklistService;
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

    // 게시물 좋아요
    @PostMapping("/posts/{postId}/likes")
    @ResponseStatus(HttpStatus.OK)
    public void likePost(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody LikePostRequestDto likePostRequestDto) {
            this.likeService.likePost(userDetails.getUser(), likePostRequestDto.getPostId());
    }

    // 로그인 사용자가 좋아요한 게시물 조회
    @GetMapping("/posts/likes")
    public ResponseEntity<List<LikePostResponseDto>> getLikePost(@AuthenticationPrincipal UserDetailsImpl userDetails) {
            return ResponseEntity.ok(this.likeService.getLikePost(userDetails.getUser()));
    }

    // 댓글 좋아요
    @PostMapping("/comments/{commentId}/likes")
    @ResponseStatus(HttpStatus.OK)
    public void likeComment( @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody LikeCommentRequestDto likeCommentRequestDto) {
            this.likeService.likeComment(userDetails.getUser(), likeCommentRequestDto.getCommentId());
    }

    // 로그인 사용자가 좋아요한 댓글 조회
    @GetMapping("/comments/likes")
    public ResponseEntity<List<LikeCommentResponseDto>> getLikeComment(@AuthenticationPrincipal UserDetailsImpl userDetails) {
            return ResponseEntity.ok(this.likeService.getLikeComment(userDetails.getUser()));
    }

    // 게시물 좋아요 취소
    @DeleteMapping("/posts/{postId}/likes")
    @ResponseStatus(HttpStatus.OK)
    public void unlikePost(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody LikePostRequestDto likePostRequestDto) {
            this.likeService.unlikePost(userDetails.getUser(), likePostRequestDto.getPostId());
    }

    // 댓글 좋아요 취소
    @DeleteMapping("/comments/{commentId}/likes")
    @ResponseStatus(HttpStatus.OK)
    public void unlikeComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody LikeCommentRequestDto likeCommentRequestDto) {
            this.likeService.unlikeComment(userDetails.getUser(), likeCommentRequestDto.getCommentId());
    }
}
