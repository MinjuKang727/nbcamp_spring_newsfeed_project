package com.sparta.newsfeed_project.domain.post.controller;

import com.sparta.newsfeed_project.auth.security.UserDetailsImpl;
import com.sparta.newsfeed_project.domain.post.dto.*;
import com.sparta.newsfeed_project.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;

//게시글 컨트롤러
@RestController
@RequiredArgsConstructor
public class PostController {
    // - **게시물 작성, 조회, 수정, 삭제 기능**
    //    - 조건
    //        - 게시물 수정, 삭제는 작성자 본인만 처리할 수 있습니다. postUser
    //        - 작성자가 아닌 다른 사용자가 게시물 수정, 삭제를 시도하는 경우 예외처리!
    //- **뉴스피드 조회 기능**
    //    - 기본 정렬은 생성일자 ****기준으로 내림차순 정렬합니다.
    //    - 10개씩 페이지네이션하여, 각 페이지 당 뉴스피드 데이터가 10개씩 나오게 합니다.
    //        - 다른 사람의 뉴스피드는 볼 수 없습니다. - 예외처리.

    private final PostService postService;

    //게시글 등록
    @PostMapping("/posts")
    public ResponseEntity<PostSaveResponseDto> savePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestBody PostSaveRequestDto postSaveRequestDto){
        System.out.println("11111"+ userDetails.getMyId()+ userDetails.getEmail()+ userDetails.getUsername()+userDetails.getUser() + userDetails.getMyId());
        return ResponseEntity.ok(postService.savePost(userDetails,postSaveRequestDto));
    }

    //게시글 조회 작성자 Id 로 게시글 전체 조회 아이디를 빼고 상세조회를 작성자 Id를 넣어야할지 얘기해볼것.
    @GetMapping("/posts")
    public ResponseEntity<List<PostSimpleResponseDto>> getPostList (){
        return ResponseEntity.ok(postService.getPostList());
    }

    //뉴스피스 조회 친구로 등록된 유저의 모든 게시물을 조회하는 기능.
    @GetMapping("/newsfeed")
    public ResponseEntity<Page<NewsfeedResponseDto>> getNewsfeedList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                     @RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                     @RequestParam(defaultValue = "10",required = false) int size){
        return ResponseEntity.ok(postService.getNewsfeedList(userDetails,pageNo,size));
    }

    //게시글 수정
    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostUpdateResponseDto> updatePost (@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                             @PathVariable Long postId,
                                                             @RequestBody PostUpdateRequestDto postUpdateRequestDto){
        return ResponseEntity.ok(postService.updatePost(userDetails,postId,postUpdateRequestDto));

    }
    //게시글 삭제
    @DeleteMapping("/posts/{postId}")
    public void deletePost(@AuthenticationPrincipal UserDetailsImpl userDetails,@PathVariable Long postId){
        postService.deletePost(userDetails,postId);
    }


}
