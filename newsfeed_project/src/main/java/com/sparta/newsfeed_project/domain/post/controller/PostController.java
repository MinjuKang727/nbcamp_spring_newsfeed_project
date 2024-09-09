package com.sparta.newsfeed_project.domain.post.controller;

import com.sparta.newsfeed_project.auth.security.UserDetailsImpl;
import com.sparta.newsfeed_project.domain.post.dto.*;
import com.sparta.newsfeed_project.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<PostSaveResponseDto> savePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestBody PostSaveRequestDto postSaveRequestDto) {
            return ResponseEntity.ok(postService.savePost(userDetails,postSaveRequestDto));
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostSimpleResponseDto>> getPostList (){
        return ResponseEntity.ok(postService.getPostList());
    }

    @GetMapping("/newsfeed")
    public ResponseEntity<Page<NewsfeedResponseDto>> getNewsfeedList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                     @RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                     @RequestParam(defaultValue = "10",required = false) int size,
                                                                     @RequestParam(defaultValue = "createdAt",required = false) String sort) {
            return ResponseEntity.ok(postService.getNewsfeedList(userDetails,pageNo,size,sort));
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostUpdateResponseDto> updatePost (@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                             @PathVariable Long postId,
                                                             @RequestBody PostUpdateRequestDto postUpdateRequestDto) {
            return ResponseEntity.ok(postService.updatePost(userDetails,postId,postUpdateRequestDto));
    }

    @DeleteMapping("/posts/{postId}")
    public void deletePost(@AuthenticationPrincipal UserDetailsImpl userDetails,@PathVariable Long postId) {
            this.postService.deletePost(userDetails,postId);
    }
}
