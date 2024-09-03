package com.sparta.newsfeed_project.domain.like.repository;

import com.sparta.newsfeed_project.domain.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
