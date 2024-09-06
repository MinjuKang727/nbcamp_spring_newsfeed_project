package com.sparta.newsfeed_project.domain.like.controller;

import com.sparta.newsfeed_project.auth.security.UserDetailsImpl;
import com.sparta.newsfeed_project.domain.common.exception.CommonException;
import com.sparta.newsfeed_project.domain.common.exception.ExceptionCode;
import com.sparta.newsfeed_project.domain.like.dto.LikeCommentRequestDto;
import com.sparta.newsfeed_project.domain.like.dto.LikeCommentResponseDto;
import com.sparta.newsfeed_project.domain.like.dto.LikePostRequestDto;
import com.sparta.newsfeed_project.domain.like.dto.LikePostResponseDto;
import com.sparta.newsfeed_project.domain.like.service.LikeService;
import com.sparta.newsfeed_project.domain.token.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    private final TokenBlacklistService tokenBlacklistService;

    // 게시물 좋아요
    @PostMapping("/like-post")
    @ResponseStatus(HttpStatus.OK)
    public void likePost(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody LikePostRequestDto likePostRequestDto) throws CommonException, IOException {
        if (!this.tokenBlacklistService.isTokenBlackListed(request)) {
            this.likeService.likePost(userDetails.getUser(), likePostRequestDto.getPostId());
            return;
        }

        throw new CommonException(ExceptionCode.FAILED_LIKE_POST, new CommonException(ExceptionCode.EXPIRED_JWT_TOKEN));
    }

    // 로그인 사용자가 좋아요한 게시물 조회
    @GetMapping("/like-post")
    public ResponseEntity<List<LikePostResponseDto>> getLikePost(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException, CommonException {
        if (!this.tokenBlacklistService.isTokenBlackListed(request)) {
            return ResponseEntity.ok(this.likeService.getLikePost(userDetails.getUser()));
        }

        throw new CommonException(ExceptionCode.FAILED_GET_LIKE_POSTLIST, new CommonException(ExceptionCode.EXPIRED_JWT_TOKEN));

    }

    // 댓글 좋아요
    @PostMapping("/like-comment")
    @ResponseStatus(HttpStatus.OK)
    public void likeComment(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody LikeCommentRequestDto likeCommentRequestDto) throws IOException, CommonException {
        if (!this.tokenBlacklistService.isTokenBlackListed(request)) {
            this.likeService.likeComment(userDetails.getUser(), likeCommentRequestDto.getCommentId());
            return;
        }

        throw new CommonException(ExceptionCode.FAILED_LIKE_COMMENT, new CommonException(ExceptionCode.EXPIRED_JWT_TOKEN));
    }

    // 로그인 사용자가 좋아요한 댓글 조회
    @GetMapping("/like-comment")
    public ResponseEntity<List<LikeCommentResponseDto>> getLikeComment(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException, CommonException {
        if (!this.tokenBlacklistService.isTokenBlackListed(request)) {
            return ResponseEntity.ok(this.likeService.getLikeComment(userDetails.getUser()));
        }

        throw new CommonException(ExceptionCode.FAILED_GET_LIKE_COMMENTLIST, new CommonException(ExceptionCode.EXPIRED_JWT_TOKEN));
    }

    // 게시물 좋아요 취소
    @DeleteMapping("/unlike-post")
    @ResponseStatus(HttpStatus.OK)
    public void unlikePost(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody LikePostRequestDto likePostRequestDto) throws IOException, CommonException {
        if (!this.tokenBlacklistService.isTokenBlackListed(request)) {
            this.likeService.unlikePost(userDetails.getUser(), likePostRequestDto.getPostId());
            return;
        }

        throw new CommonException(ExceptionCode.FAILED_UNLIKE_POST, new CommonException(ExceptionCode.EXPIRED_JWT_TOKEN));
    }

    // 댓글 좋아요 취소
    @DeleteMapping("/unlike-comment")
    @ResponseStatus(HttpStatus.OK)
    public void unlikeComment(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody LikeCommentRequestDto likeCommentRequestDto) throws IOException, CommonException {
        if (!this.tokenBlacklistService.isTokenBlackListed(request)) {
            this.likeService.unlikeComment(userDetails.getUser(), likeCommentRequestDto.getCommentId());
            return;
        }

        throw new CommonException(ExceptionCode.FAILED_UNLIKE_COMMENT, new CommonException(ExceptionCode.EXPIRED_JWT_TOKEN));
    }
}
