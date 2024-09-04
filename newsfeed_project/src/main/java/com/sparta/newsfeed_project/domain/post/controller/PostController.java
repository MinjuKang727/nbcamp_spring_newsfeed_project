package com.sparta.newsfeed_project.domain.post.controller;

import com.sparta.newsfeed_project.domain.post.dto.*;
import com.sparta.newsfeed_project.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/post")
    public ResponseEntity<PostSaveResponseDto> savePost(@RequestBody PostSaveRequestDto postSaveRequestDto){
        return ResponseEntity.ok(postService.savePost(postSaveRequestDto));
    }

    //게시글 조회 작성자 Id 로 게시글 전체 조회 아이디를 빼고 상세조회를 작성자 Id를 넣어야할지 얘기해볼것.
    @GetMapping("/post")
    public ResponseEntity<List<PostSimpleResponseDto>> getPostList (){
        return ResponseEntity.ok(postService.getPostList());
    }

    //뉴스피스 조회 친구로 등록된 유저의 모든 게시물을 조회하는 기능.
    @GetMapping("/post/friends")
    public ResponseEntity<List<NewsfeedResponseDto>> getNewsfeedList(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                     @RequestParam(defaultValue = "10",required = false) int size){
        return ResponseEntity.ok(postService.getNewsfeedList(pageNo,size));
    }

    //게시글 수정
    @PutMapping("/post/{postId}")
    public ResponseEntity<PostUpdateResponseDto> updatePost (@PathVariable Long postId,
                                                             @RequestBody PostUpdateRequestDto postUpdateRequestDto){
        return ResponseEntity.ok(postService.updatePost(postId,postUpdateRequestDto));

    }
    //게시글 삭제
    @DeleteMapping("/post/{postId}")
    public void deletePost(@PathVariable Long postId){
        postService.deletePost(postId);
    }


}
