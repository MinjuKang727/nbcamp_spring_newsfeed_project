package com.sparta.newsfeed_project.domain.friend.repository;

import com.sparta.newsfeed_project.domain.friend.entity.Friend;
import com.sparta.newsfeed_project.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findFriendsByFollowedUser(User me);
    Friend findByFollowingUserAndFollowedUser(User user1, User user2);

<<<<<<< HEAD
    List<Friend> findFriendsByFollowingUser(User me);
=======
    List<Friend> findFriendsByFollowingUser(User user);
>>>>>>> 181b7cebcf2c36d0beefc86becd157fbd3d2e6de
}
