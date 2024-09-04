package com.sparta.newsfeed_project.domain.friend.controller;

import com.sparta.newsfeed_project.auth.security.UserDetailsImpl;
import com.sparta.newsfeed_project.domain.friend.dto.FriendIdRequestDto;
import com.sparta.newsfeed_project.domain.friend.dto.FriendEmailRequestDto;
import com.sparta.newsfeed_project.domain.friend.dto.FollowerResponseDto;
import com.sparta.newsfeed_project.domain.friend.service.FriendService;
import com.sparta.newsfeed_project.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/follow")
    @ResponseStatus(HttpStatus.OK)
    public void followUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody FriendEmailRequestDto friendEmailRequestDto) {
        friendService.followUser(userDetails.getUser(), friendEmailRequestDto.getEmail());
    }

    @GetMapping("/friends")
    public ResponseEntity<List<FollowerResponseDto>> getFollowerList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(friendService.getFollowerList(userDetails.getUser()));
    }

    @PostMapping("/allow-following")
    @ResponseStatus(HttpStatus.OK)
    public void allowFollowing(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody FriendIdRequestDto friendIdRequestDto) {
        friendService.allowFollowing(userDetails.getUser(), friendIdRequestDto.getId());
    }

    @DeleteMapping("/unfollow")
    @ResponseStatus(HttpStatus.OK)
    public void unfollow(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody FriendIdRequestDto friendIdRequestDto) {
        friendService.unfollow(userDetails.getUser(), friendIdRequestDto.getId());
    }
}
