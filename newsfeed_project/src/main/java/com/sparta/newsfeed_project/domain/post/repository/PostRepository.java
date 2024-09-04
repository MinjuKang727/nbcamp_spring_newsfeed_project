package com.sparta.newsfeed_project.domain.post.repository;

import com.sparta.newsfeed_project.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
