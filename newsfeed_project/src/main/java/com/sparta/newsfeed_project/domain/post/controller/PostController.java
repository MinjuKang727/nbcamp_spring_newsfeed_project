package com.sparta.newsfeed_project.domain.post.controller;

import com.sparta.newsfeed_project.auth.security.UserDetailsImpl;
import com.sparta.newsfeed_project.domain.common.exception.CommonException;
import com.sparta.newsfeed_project.domain.common.exception.ExceptionCode;
import com.sparta.newsfeed_project.domain.post.dto.*;
import com.sparta.newsfeed_project.domain.post.service.PostService;
import com.sparta.newsfeed_project.domain.token.Token;
import com.sparta.newsfeed_project.domain.token.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final TokenBlacklistService tokenBlacklistService;

    @PostMapping("/posts")
    public ResponseEntity<PostSaveResponseDto> savePost(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestBody PostSaveRequestDto postSaveRequestDto) throws IOException, CommonException {
        if (!this.tokenBlacklistService.isTokenBlackListed(request)) {
            return ResponseEntity.ok(postService.savePost(userDetails,postSaveRequestDto));
        }

        throw new CommonException(ExceptionCode.FAILED_SAVE_POST, new CommonException(ExceptionCode.EXPIRED_JWT_TOKEN));
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostSimpleResponseDto>> getPostList (){
        return ResponseEntity.ok(postService.getPostList());
    }

    @GetMapping("/newsfeed")
    public ResponseEntity<Page<NewsfeedResponseDto>> getNewsfeedList(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                     @RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                     @RequestParam(defaultValue = "10",required = false) int size,
                                                                     @RequestParam(defaultValue = "createdAt",required = false) String sort) throws IOException, CommonException {
        if (!this.tokenBlacklistService.isTokenBlackListed(request)) {
            return ResponseEntity.ok(postService.getNewsfeedList(userDetails,pageNo,size,sort));
        }

        throw new CommonException(ExceptionCode.FAILED_GET_NEWSFEEDLIST, new CommonException(ExceptionCode.EXPIRED_JWT_TOKEN));
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostUpdateResponseDto> updatePost (HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                             @PathVariable Long postId,
                                                             @RequestBody PostUpdateRequestDto postUpdateRequestDto) throws IOException, CommonException {
        if (!this.tokenBlacklistService.isTokenBlackListed(request)) {
            return ResponseEntity.ok(postService.updatePost(userDetails,postId,postUpdateRequestDto));
        }

        throw new CommonException(ExceptionCode.FAILED_UPDATE_POST, new CommonException(ExceptionCode.EXPIRED_JWT_TOKEN));
    }

    @DeleteMapping("/posts/{postId}")
    public void deletePost(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails,@PathVariable Long postId) throws IOException, CommonException {
        if (!this.tokenBlacklistService.isTokenBlackListed(request)) {
            this.postService.deletePost(userDetails,postId);
            return;
        }

        throw new CommonException(ExceptionCode.FAILED_DELETE_POST, new CommonException(ExceptionCode.EXPIRED_JWT_TOKEN));
    }


}
