package com.sparta.newsfeed_project.domain.comment.controller;


import com.sparta.newsfeed_project.domain.comment.dto.*;
import com.sparta.newsfeed_project.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {


    private final CommentService commentService;

    //댓글 등록
    //튜터님께 여쭤본 후 URI 수정할 수 있도록
    @PostMapping("/posts/{post_id}/comments")
    public ResponseEntity<CommentSaveResponseDto> saveComment (@PathVariable Long post_id,
                                                               @RequestBody CommentSaveRequestDto commentSaveRequestDto){
        return ResponseEntity.ok(commentService.saveComment(post_id,commentSaveRequestDto));
    }

    //댓글 조회
    @GetMapping("/posts/{post_id}/comments")
    public ResponseEntity<List<CommentSimpleResponseDto>> getCommentList (@PathVariable Long post_id) {
        return ResponseEntity.ok(commentService.getCommentList(post_id));
    }

    // 댓글 수정
    @PutMapping("/posts/{post_id}/comments/{comment_id}")
    public ResponseEntity<CommentUpdateResponseDto> updateComment (@PathVariable Long post_id,
                                                                   @PathVariable Long comment_id,
                                                                   @RequestBody CommentUpdateRequestDto commentUpdateRequestDto){
        return ResponseEntity.ok(commentService.updateComment(post_id,comment_id,commentUpdateRequestDto));
    }

    //댓글 삭제
    @DeleteMapping("/posts/{post_id}/comments/{comment_id}")
    public void deleteComment(@PathVariable Long post_id, @PathVariable Long comment_id){
        commentService.deleteComment(post_id,comment_id);
    }
}
