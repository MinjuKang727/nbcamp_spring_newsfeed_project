package com.sparta.newsfeed_project.domain.like.repository;

import com.sparta.newsfeed_project.domain.comment.entity.Comment;
import com.sparta.newsfeed_project.domain.like.entity.Like;
import com.sparta.newsfeed_project.domain.post.entity.Post;
import com.sparta.newsfeed_project.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByUserAndComment(User user, Comment entity);

    Like findByUserAndPost(User user, Post entity);
}
