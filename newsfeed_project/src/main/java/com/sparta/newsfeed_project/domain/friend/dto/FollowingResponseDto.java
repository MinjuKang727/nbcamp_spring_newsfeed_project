package com.sparta.newsfeed_project.domain.friend.dto;

import com.sparta.newsfeed_project.domain.friend.entity.Friend;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FollowingResponseDto {
    private final String username;
    private final String email;
    private final boolean allowed;

    public FollowingResponseDto(Friend friend) {
        this.username = friend.getFollowingUser().getUsername();
        this.email = friend.getFollowingUser().getEmail();
        this.allowed = friend.isAccepted();
    }
}
