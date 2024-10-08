package com.sparta.newsfeed_project.domain.friend.service;

import com.sparta.newsfeed_project.domain.friend.dto.FollowedResponseDto;
import com.sparta.newsfeed_project.domain.friend.dto.FollowingResponseDto;
import com.sparta.newsfeed_project.domain.friend.entity.Friend;
import com.sparta.newsfeed_project.domain.friend.repository.FriendRepository;
import com.sparta.newsfeed_project.domain.user.entity.User;
import com.sparta.newsfeed_project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
        );
    }

    private Friend findFriend(User me, User other, boolean isFollowing) {
        return isFollowing
                ? friendRepository.findByFollowingUserAndFollowedUser(me, other)
                : friendRepository.findByFollowingUserAndFollowedUser(other, me);
    }

    public FollowedResponseDto followUser(User me, String followingEmail) {
        if (Objects.equals(me.getEmail(), followingEmail)) {
            throw new IllegalArgumentException("자신의 계정은 팔로우 할 수 없습니다.");
        }

        User followingUser = userRepository.findUserByEmail(followingEmail).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );
        if(findFriend(me, followingUser, true) != null) {
            throw new IllegalArgumentException("이미 팔로우한 사용자입니다.");
        }

        Friend friend = new Friend(me, followingUser);
        friendRepository.save(friend);
        return new FollowedResponseDto(friend);
    }

    @Transactional(readOnly = true)
    public List<FollowedResponseDto> getFollowerList(User me) {
        List<Friend> followerList = friendRepository.findFriendsByFollowingUser(me);
        List<FollowedResponseDto> followedResponseDtoList = new ArrayList<>();
        for (Friend follower : followerList) {
            followedResponseDtoList.add(new FollowedResponseDto(follower));
        }
        return followedResponseDtoList;
    }

    public FollowingResponseDto allowFollowing(User me, Long followerId) {
        User follower = findUser(followerId);
        Friend friend = findFriend(me, follower, false);

        if(friend == null) {
            throw new IllegalArgumentException("팔로워를 찾을 수 없습니다.");
        }
        if(!friend.isAccepted()) {
            friend.acceptFollow();
            return new FollowingResponseDto(friend);
        }
        return null;
    }

    public void unfollow(User me, Long followingId) {
        User follower = findUser(followingId);
        Friend friend = findFriend(me, follower, true);

        if(friend == null) {
            throw new IllegalArgumentException("팔로잉한 유저를 찾을 수 없습니다.");
        }

        friendRepository.delete(friend);
    }
}
