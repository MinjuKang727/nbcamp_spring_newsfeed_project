package com.sparta.newsfeed_project.domain.comment.controller;


import com.sparta.newsfeed_project.auth.security.UserDetailsImpl;
import com.sparta.newsfeed_project.domain.comment.dto.*;
import com.sparta.newsfeed_project.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{post_id}/comments")
    public ResponseEntity<CommentSaveResponseDto> saveComment (@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                               @PathVariable Long post_id,
                                                               @RequestBody CommentSaveRequestDto commentSaveRequestDto) {
            return ResponseEntity.ok(commentService.saveComment(userDetails.getUser(), post_id, commentSaveRequestDto));
    }

    @GetMapping("/posts/{post_id}/comments")
    public ResponseEntity<List<CommentSimpleResponseDto>> getCommentList (@PathVariable Long post_id) {
            return ResponseEntity.ok(commentService.getCommentList(post_id));
    }

    @PutMapping("/posts/{post_id}/comments/{comment_id}")
    public ResponseEntity<CommentUpdateResponseDto> updateComment (@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                   @PathVariable Long post_id,
                                                                   @PathVariable Long comment_id,
                                                                   @RequestBody CommentUpdateRequestDto commentUpdateRequestDto) {
            return ResponseEntity.ok(commentService.updateComment(userDetails,post_id,comment_id,commentUpdateRequestDto));
    }

    @DeleteMapping("/posts/{post_id}/comments/{comment_id}")
    public void deleteComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                              @PathVariable Long post_id,
                              @PathVariable Long comment_id) {
        this.commentService.deleteComment(userDetails,post_id,comment_id);
    }
}
