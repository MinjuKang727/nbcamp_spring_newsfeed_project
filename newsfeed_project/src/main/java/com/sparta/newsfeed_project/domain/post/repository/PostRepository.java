package com.sparta.newsfeed_project.domain.post.repository;

import com.sparta.newsfeed_project.domain.post.entity.Post;
import com.sparta.newsfeed_project.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
   public Page<Post> findByUserIdIn(List<Long> userIds, Pageable pageable);
}
