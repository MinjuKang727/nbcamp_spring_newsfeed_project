package com.sparta.newsfeed_project.domain.friend.repository;

import com.sparta.newsfeed_project.domain.friend.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {
}
