package com.sparta.newsfeed_project.domain.post.service;

import com.sparta.newsfeed_project.auth.security.UserDetailsImpl;
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

    // 게시물 등록
    @Transactional
    public PostSaveResponseDto savePost(UserDetailsImpl userDetails, PostSaveRequestDto postSaveRequestDto) {
        System.out.println("11111"+ userDetails.getUser().getId());
        //게시물 생성
        Post post = new Post(postSaveRequestDto.getTitle(), postSaveRequestDto.getContent(),userDetails.getUser());
        //게시물 저장
        Post savedPost = postRepository.save(post);
        UserDto userDto =new UserDto(userDetails.getMyId(),userDetails.getEmail(),userDetails.getUsername());

        return new PostSaveResponseDto(
                //게시물 작성자
                userDto,
                savedPost.getTitle(),
                savedPost.getContent()
        );
    }

    // 게시물 조회
    public List<PostSimpleResponseDto> getPostList() {

        // 게시글이 있는지 없는지 확인. 예외처리 방법??
        List<Post> posts = postRepository.findAll();

        List<PostSimpleResponseDto> simpleDtoList = new ArrayList<>();

        for (Post post : posts) {
            PostSimpleResponseDto dto = new PostSimpleResponseDto(post.getPostId(),post.getTitle(), post.getContent(), post.getCreatedAt(), post.getModifiedAt());
            simpleDtoList.add(dto);
        }
        return simpleDtoList;
    }

    //게시물 수정
    @Transactional
    public PostUpdateResponseDto updatePost(UserDetailsImpl userDetails, Long postId, PostUpdateRequestDto postUpdateRequestDto) {

        //게시물이 있는지 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NullPointerException("게시글을 찾을 수 없습니다."));
        UserDto userDto =new UserDto(post.getUser().getId(),post.getUser().getEmail(),post.getUser().getUsername());
        //post 를 통해 찾은 유저Id 와 게시물 수정을 시도하는 유저의 Id 가 같은지 확인.
        if (!post.getUser().getId().equals(userDetails.getMyId())){
            throw new RuntimeException("게시물 수정은 작성자 본인만 처리할 수 있습니다.");
        }
        // 수정 메서드
        post.update(postUpdateRequestDto.getTitle(),postUpdateRequestDto.getContent());

        return new PostUpdateResponseDto(userDto ,postId,post.getTitle(),post.getContent(),post.getCreatedAt(),post.getModifiedAt());
    }

    //게시물 삭제
    @Transactional
    public void deletePost(UserDetailsImpl userDetails,Long postId) {
        //게시물이 있는지 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NullPointerException("게시글을 찾을 수 없습니다."));
        //게시물 작성자 본인인지 확인
        if (!post.getUser().getId().equals(userDetails.getMyId())){
            throw new RuntimeException("게시물 삭제는 작성자 본인만 처리할 수 있습니다.");
        }

        //게시물 삭제
        postRepository.delete(post);
    }

    //뉴스피드 조회
    public List<NewsfeedResponseDto> getNewsfeedList(UserDetailsImpl userDetails,int pageNo, int size) {
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by("createdAt").descending());
        // 팔로우 한 유저만 제한. = 뉴스피드
     Page<NewsfeedResponseDto> newsfeeds = postRepository.findAll(pageable).map(NewsfeedResponseDto::new);

     // 팔로잉하고 있는 유저의 게시물만 볼 수 있도록 하면 뉴스피드???
//        List<Friend> friends = friendRepository.findAll();
//        for (Friend friend : friends){
//
//        }
//        // myId.getFollowingUser.
//        }


        return newsfeeds.getContent();
    }
}
