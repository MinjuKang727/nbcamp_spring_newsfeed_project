package com.sparta.newsfeed_project.domain.post.service;

import com.sparta.newsfeed_project.auth.security.UserDetailsImpl;
import com.sparta.newsfeed_project.domain.friend.entity.Friend;
import com.sparta.newsfeed_project.domain.friend.repository.FriendRepository;
import com.sparta.newsfeed_project.domain.post.dto.*;
import com.sparta.newsfeed_project.domain.post.entity.Post;
import com.sparta.newsfeed_project.domain.post.repository.PostRepository;
import com.sparta.newsfeed_project.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AuthorizationServiceException;
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

    @Transactional
    public PostSaveResponseDto savePost(UserDetailsImpl userDetails, PostSaveRequestDto postSaveRequestDto) {
        Post post = new Post(postSaveRequestDto.getTitle(), postSaveRequestDto.getContent(),userDetails.getUser());
        Post savedPost = postRepository.save(post);
        UserDto userDto =new UserDto(userDetails.getMyId(),userDetails.getEmail(),userDetails.getUsername());

        return new PostSaveResponseDto(
                userDto,
                savedPost.getTitle(),
                savedPost.getContent(),
                savedPost.getCreatedAt(),
                savedPost.getModifiedAt()
        );
    }

    public List<PostSimpleResponseDto> getPostList() {
        return postRepository.getAllPostWithLikesCount();
    }

    @Transactional
    public PostUpdateResponseDto updatePost(UserDetailsImpl userDetails, Long postId, PostUpdateRequestDto postUpdateRequestDto) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        UserDto userDto =new UserDto(post.getUser().getId(),post.getUser().getEmail(),post.getUser().getUsername());

        if (!post.getUser().getId().equals(userDetails.getMyId())){
            throw new AuthorizationServiceException("게시물 수정은 작성자 본인만 처리할 수 있습니다.");
        }
        post.update(postUpdateRequestDto.getTitle(),postUpdateRequestDto.getContent());

        return new PostUpdateResponseDto(userDto ,postId,post.getTitle(),post.getContent(),post.getCreatedAt(),post.getModifiedAt());
    }

    @Transactional
    public void deletePost(UserDetailsImpl userDetails,Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        if (!post.getUser().getId().equals(userDetails.getMyId())){
            throw new AuthorizationServiceException("게시물 삭제는 작성자 본인만 처리할 수 있습니다.");
        }

        postRepository.delete(post);
    }

    public Page<NewsfeedResponseDto> getNewsfeedList(UserDetailsImpl userDetails,int pageNo, int size,String sort) {
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(sort).descending());

        List<Long> followingIds = getFollowingUserIdList(userDetails.getUser());
        followingIds.add(userDetails.getMyId());

        Page<Post> followingIdsPost = postRepository.findByUserIdIn(followingIds,pageable);

        return followingIdsPost.map(NewsfeedResponseDto::new);
    }
}
