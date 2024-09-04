package com.sparta.newsfeed_project.domain.post.service;

import com.sparta.newsfeed_project.domain.friend.entity.Friend;
import com.sparta.newsfeed_project.domain.friend.repository.FriendRepository;
import com.sparta.newsfeed_project.domain.post.dto.*;
import com.sparta.newsfeed_project.domain.post.entity.Post;
import com.sparta.newsfeed_project.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final FriendRepository friendRepository;

    // 게시물 등록
    @Transactional
    public PostSaveResponseDto savePost(PostSaveRequestDto postSaveRequestDto) {
        //게시물 생성
        Post post = new Post(postSaveRequestDto.getTitle(), postSaveRequestDto.getContent(), postSaveRequestDto.getPostUser());
        //게시물 저장
        Post savedPost = postRepository.save(post);
        UserDto userDto =new UserDto(savedPost.getUser().getId(),savedPost.getUser().getEmail(),savedPost.getUser().getUsername());

        return new PostSaveResponseDto(
                //게시물 작성자
                userDto,
                savedPost.getTitle(),
                savedPost.getContent()
        );
    }

    // 게시글 조회
    public List<PostSimpleResponseDto> getPostList() {

        // 게시글이 있는지 없는지 확인. 예외처리 방법??
        List<Post> posts = postRepository.findAll();

        List<PostSimpleResponseDto> simpleDtoList = new ArrayList<>();

        for (Post post : posts) {
            PostSimpleResponseDto dto = new PostSimpleResponseDto(post.getTitle(), post.getContent(), post.getCreatedAt(), post.getModifiedAt());
            simpleDtoList.add(dto);
        }
        return simpleDtoList;
    }

    //게시글 수정
    @Transactional
    public PostUpdateResponseDto updatePost(Long postId,PostUpdateRequestDto postUpdateRequestDto) {

        //게시글이 있는지 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NullPointerException("게시글을 찾을 수 없습니다."));
        UserDto userDto =new UserDto(post.getUser().getId(),post.getUser().getEmail(),post.getUser().getUsername());
        //post 를 통해 찾은 유저Id 와 게시물 수정을 시도하는 유저의 Id 가 같은지 확인.
//        if (post.getUser().getId().equals())(
//
//                )
        // 수정 메서드
        post.update(postUpdateRequestDto.getTitle(),postUpdateRequestDto.getContent());

        return new PostUpdateResponseDto(postId,post.getTitle(),post.getContent(),userDto,post.getCreatedAt());
    }

    //게시글 삭제
    @Transactional
    public void deletePost(Long postId) {
        //게시글이 있는지 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NullPointerException("게시글을 찾을 수 없습니다."));
        //post 를 통해 찾은 유저Id 와 게시물 수정을 시도하는 유저의 Id 가 같은지 확인.
//        if (post.getUser().getId().equals())(
//
//                )
        //게시글 삭제
        postRepository.delete(post);
    }

    //뉴스피드 조회
    public List<NewsfeedResponseDto> getNewsfeedList(int pageNo, int size) {
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by("createdAt").descending());
        // 팔로우 한 유저만 제한. = 뉴스피드
     Page<NewsfeedResponseDto> newsfeeds = postRepository.findAll(pageable).map(NewsfeedResponseDto::new);

//        List<Friend> friends = friendRepository.findAll();
//        for (Friend friend : friends){
//
//        }
//        // myId.getFollowingUser.
//        }


        return newsfeeds.getContent();
    }
}
