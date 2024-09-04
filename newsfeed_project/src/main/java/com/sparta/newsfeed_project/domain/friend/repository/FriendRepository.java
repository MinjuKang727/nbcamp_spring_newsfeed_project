package com.sparta.newsfeed_project.domain.friend.repository;

import com.sparta.newsfeed_project.domain.friend.entity.Friend;
import com.sparta.newsfeed_project.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findFriendsByFollowedUser(User me);
    Friend findByFollowingUserAndFollowedUser(User user1, User user2);
}
