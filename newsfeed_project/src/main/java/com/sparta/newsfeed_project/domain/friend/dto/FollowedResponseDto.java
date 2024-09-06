package com.sparta.newsfeed_project.domain.friend.dto;

import com.sparta.newsfeed_project.domain.friend.entity.Friend;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FollowedResponseDto {
    private final String username;
    private final String email;
    private final boolean allowed;

    public FollowedResponseDto(Friend friend) {
        this.username = friend.getFollowedUser().getUsername();
        this.email = friend.getFollowedUser().getEmail();
        this.allowed = friend.isAccepted();
    }
}
