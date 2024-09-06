package com.sparta.newsfeed_project.domain.friend.controller;

import com.sparta.newsfeed_project.auth.security.UserDetailsImpl;
import com.sparta.newsfeed_project.domain.common.exception.CommonException;
import com.sparta.newsfeed_project.domain.common.exception.ExceptionCode;
import com.sparta.newsfeed_project.domain.friend.dto.*;
import com.sparta.newsfeed_project.domain.friend.service.FriendService;
import com.sparta.newsfeed_project.domain.token.TokenBlacklistService;
import com.sparta.newsfeed_project.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;
    private final TokenBlacklistService tokenBlacklistService;

    // 팔로우 신청
    @PostMapping("/follow")
    public ResponseEntity<FollowedResponseDto> followUser(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody FriendEmailRequestDto friendEmailRequestDto) throws IOException, CommonException {
        if (!this.tokenBlacklistService.isTokenBlackListed(request)) {
            return ResponseEntity.ok(friendService.followUser(userDetails.getUser(), friendEmailRequestDto.getEmail()));
        }

        throw new CommonException(ExceptionCode.FAILED_FOLLOW_USER, new CommonException(ExceptionCode.EXPIRED_JWT_TOKEN));
    }

    // 팔로우 사용자 목록 조회
    @GetMapping("/friends")
    public ResponseEntity<List<FollowedResponseDto>> getFollowerList(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException, CommonException {
        if (!this.tokenBlacklistService.isTokenBlackListed(request)) {
            return ResponseEntity.ok(friendService.getFollowerList(userDetails.getUser()));
        }

        throw new CommonException(ExceptionCode.FAILED_GET_FOLLOWLIST, new CommonException(ExceptionCode.EXPIRED_JWT_TOKEN));
    }

    // 팔로우 수락
    @PostMapping("/allow-following")
    public ResponseEntity<FollowingResponseDto> allowFollowing(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody FriendIdRequestDto friendIdRequestDto) throws IOException, CommonException {
        if (!this.tokenBlacklistService.isTokenBlackListed(request)) {
            return ResponseEntity.ok(friendService.allowFollowing(userDetails.getUser(), friendIdRequestDto.getId()));
        }

        throw new CommonException(ExceptionCode.FAILED_ALLOW_FOLLOWING, new CommonException(ExceptionCode.EXPIRED_JWT_TOKEN));
    }

    // 팔로우 취소
    @DeleteMapping("/unfollow")
    @ResponseStatus(HttpStatus.OK)
    public void unfollow(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody FriendIdRequestDto friendIdRequestDto) throws IOException, CommonException {
        if (!this.tokenBlacklistService.isTokenBlackListed(request)) {
            this.friendService.unfollow(userDetails.getUser(), friendIdRequestDto.getId());
            return;
        }

        throw new CommonException(ExceptionCode.FAILED_UNFOLLOW, new CommonException(ExceptionCode.EXPIRED_JWT_TOKEN));
    }
}
