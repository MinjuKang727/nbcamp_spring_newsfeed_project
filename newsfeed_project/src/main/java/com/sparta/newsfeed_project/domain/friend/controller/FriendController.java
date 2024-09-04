package com.sparta.newsfeed_project.domain.friend.controller;

import com.sparta.newsfeed_project.domain.friend.dto.FriendIdRequestDto;
import com.sparta.newsfeed_project.domain.friend.dto.FriendEmailRequestDto;
import com.sparta.newsfeed_project.domain.friend.dto.FollowerResponseDto;
import com.sparta.newsfeed_project.domain.friend.service.FriendService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/follow")
    @ResponseStatus(HttpStatus.OK)
    public void followUser(@RequestBody FriendEmailRequestDto friendEmailRequestDto, HttpServletRequest httpRequest) {
        Long myId = (Long) httpRequest.getAttribute("myId");
        friendService.followUser(myId, friendEmailRequestDto.getEmail());
    }

    @GetMapping("/friends")
    public ResponseEntity<List<FollowerResponseDto>> getFollowerList(HttpServletRequest httpRequest) {
        Long myId = (Long) httpRequest.getAttribute("myId");
        return ResponseEntity.ok(friendService.getFollowerList(myId));
    }

    @PostMapping("/allow-following")
    @ResponseStatus(HttpStatus.OK)
    public void allowFollowing(@RequestBody FriendIdRequestDto friendIdRequestDto, HttpServletRequest httpRequest) {
        Long myId = (Long) httpRequest.getAttribute("myId");
        friendService.allowFollowing(myId, friendIdRequestDto.getId());
    }

    @DeleteMapping("/unfollow")
    @ResponseStatus(HttpStatus.OK)
    public void unfollow(@RequestBody FriendIdRequestDto friendIdRequestDto, HttpServletRequest httpRequest) {
        Long myId = (Long) httpRequest.getAttribute("myId");
        friendService.unfollow(myId, friendIdRequestDto.getId());
    }
}
