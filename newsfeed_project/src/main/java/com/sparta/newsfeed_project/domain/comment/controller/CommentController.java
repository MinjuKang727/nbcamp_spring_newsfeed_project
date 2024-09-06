package com.sparta.newsfeed_project.domain.comment.controller;


import com.sparta.newsfeed_project.auth.security.UserDetailsImpl;
import com.sparta.newsfeed_project.domain.comment.dto.*;
import com.sparta.newsfeed_project.domain.comment.service.CommentService;
import com.sparta.newsfeed_project.domain.common.exception.CommonException;
import com.sparta.newsfeed_project.domain.common.exception.ExceptionCode;
import com.sparta.newsfeed_project.domain.token.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {


    private final CommentService commentService;
    private final TokenBlacklistService tokenBlacklistService;

    //댓글 등록
    //튜터님께 여쭤본 후 URI 수정할 수 있도록
    @PostMapping("/posts/{post_id}/comments")
    public ResponseEntity<CommentSaveResponseDto> saveComment (HttpServletRequest request,
                                                               @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                               @PathVariable Long post_id,
                                                               @RequestBody CommentSaveRequestDto commentSaveRequestDto) throws CommonException, IOException {
        if (!this.tokenBlacklistService.isTokenBlackListed(request)) {
            return ResponseEntity.ok(commentService.saveComment(userDetails.getUser(), post_id,commentSaveRequestDto));
        }

        throw new CommonException(ExceptionCode.FAILED_SAVE_COMMENT, new CommonException(ExceptionCode.EXPIRED_JWT_TOKEN));
    }

    //댓글 조회
    @GetMapping("/posts/{post_id}/comments")
    public ResponseEntity<List<CommentSimpleResponseDto>> getCommentList (HttpServletRequest request, @PathVariable Long post_id) throws CommonException, IOException {
            return ResponseEntity.ok(commentService.getCommentList(post_id));
    }

    // 댓글 수정
    @PutMapping("/posts/{post_id}/comments/{comment_id}")
    public ResponseEntity<CommentUpdateResponseDto> updateComment (HttpServletRequest request,
                                                                   @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                   @PathVariable Long post_id,
                                                                   @PathVariable Long comment_id,
                                                                   @RequestBody CommentUpdateRequestDto commentUpdateRequestDto) throws CommonException, IOException {
        if (!this.tokenBlacklistService.isTokenBlackListed(request)) {
            return ResponseEntity.ok(commentService.updateComment(userDetails,post_id,comment_id,commentUpdateRequestDto));
        }

        throw new CommonException(ExceptionCode.FAILED_UPDATE_COMMENT, new CommonException(ExceptionCode.EXPIRED_JWT_TOKEN));

    }

    //댓글 삭제
    @DeleteMapping("/posts/{post_id}/comments/{comment_id}")
    public void deleteComment(HttpServletRequest request,
                              @AuthenticationPrincipal UserDetailsImpl userDetails,
                              @PathVariable Long post_id,
                              @PathVariable Long comment_id) throws CommonException, IOException {
        if (!this.tokenBlacklistService.isTokenBlackListed(request)) {
            this.commentService.deleteComment(userDetails,post_id,comment_id);
            return;
        }

        throw new CommonException(ExceptionCode.FAILED_DELETE_COMMENT, new CommonException(ExceptionCode.EXPIRED_JWT_TOKEN));
    }
}
