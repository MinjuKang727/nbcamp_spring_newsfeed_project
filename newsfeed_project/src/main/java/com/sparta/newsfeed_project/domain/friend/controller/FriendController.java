package com.sparta.newsfeed_project.domain.friend.controller;

import com.sparta.newsfeed_project.auth.security.UserDetailsImpl;
import com.sparta.newsfeed_project.domain.friend.dto.*;
import com.sparta.newsfeed_project.domain.friend.service.FriendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    // 팔로우 신청
    @PostMapping("/follow")
    public ResponseEntity<FollowedResponseDto> followUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody FriendEmailRequestDto friendEmailRequestDto) {
            return ResponseEntity.ok(friendService.followUser(userDetails.getUser(), friendEmailRequestDto.getEmail()));
    }

    // 팔로우 사용자 목록 조회
    @GetMapping("/friends")
    public ResponseEntity<List<FollowedResponseDto>> getFollowerList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
            return ResponseEntity.ok(friendService.getFollowerList(userDetails.getUser()));
    }

    // 팔로우 수락
    @PostMapping("/allow-following")
    public ResponseEntity<FollowingResponseDto> allowFollowing(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody FriendIdRequestDto friendIdRequestDto) {
            return ResponseEntity.ok(friendService.allowFollowing(userDetails.getUser(), friendIdRequestDto.getId()));
    }

    // 팔로우 취소
    @DeleteMapping("/unfollow")
    @ResponseStatus(HttpStatus.OK)
    public void unfollow(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody FriendIdRequestDto friendIdRequestDto) {
            this.friendService.unfollow(userDetails.getUser(), friendIdRequestDto.getId());
    }
}
