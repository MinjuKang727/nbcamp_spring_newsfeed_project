package com.sparta.newsfeed_project.domain.post.service;

import com.sparta.newsfeed_project.auth.security.UserDetailsImpl;
import com.sparta.newsfeed_project.domain.friend.entity.Friend;
import com.sparta.newsfeed_project.domain.friend.repository.FriendRepository;
import com.sparta.newsfeed_project.domain.post.dto.*;
import com.sparta.newsfeed_project.domain.post.entity.Post;
import com.sparta.newsfeed_project.domain.post.repository.PostRepository;
import com.sparta.newsfeed_project.domain.user.entity.User;
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

    private List<Long> getFollowingUserIdList (User user) {
        List<Friend> followerList = friendRepository.findFriendsByFollowingUser(user);
        List<Long> followingUserIds = new ArrayList<>();
        for (Friend follower : followerList) {
            followingUserIds.add(follower.getFollowedUser().getId());
        }
        return followingUserIds;
    }

    // 게시물 등록
    @Transactional
    public PostSaveResponseDto savePost(UserDetailsImpl userDetails, PostSaveRequestDto postSaveRequestDto) {
        //게시물 생성
        Post post = new Post(postSaveRequestDto.getTitle(), postSaveRequestDto.getContent(),userDetails.getUser());
        //게시물 저장
        Post savedPost = postRepository.save(post);
        UserDto userDto =new UserDto(userDetails.getMyId(),userDetails.getEmail(),userDetails.getUsername());

        return new PostSaveResponseDto(
                //게시물 작성자
                userDto,
                savedPost.getTitle(),
                savedPost.getContent(),
                savedPost.getCreatedAt(),
                savedPost.getModifiedAt()
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
    public Page<NewsfeedResponseDto> getNewsfeedList(UserDetailsImpl userDetails,int pageNo, int size) {
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by("createdAt").descending());
        // 팔로우 한 유저만 제한. = 뉴스피드

        //내가 팔로우 하고 있는 아이디 들의 게시물 모음 + 내 아이디
        List<Long> followingIds = getFollowingUserIdList(userDetails.getUser());
        followingIds.add(userDetails.getMyId());

        //위에서 정리한 유저들의 Id로 Post 를 찾음
        Page<Post> followingIdsPost = postRepository.findByUserIdIn(followingIds,pageable);

        //Stream Api
        Page<NewsfeedResponseDto> result = followingIdsPost.map(post -> {
            return new NewsfeedResponseDto(post);
        });

     return result;
    }
}
