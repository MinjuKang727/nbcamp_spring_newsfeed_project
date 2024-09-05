package com.sparta.newsfeed_project.domain.post.repository;

import com.sparta.newsfeed_project.domain.post.dto.PostSimpleResponseDto;
import com.sparta.newsfeed_project.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    public Page<Post> findByUserIdIn(List<Long> userIds, Pageable pageable);

    @Query("SELECT new com.sparta.newsfeed_project.domain.post.dto.PostSimpleResponseDto(p.postId, p.title, p.content, p.createdAt, p.modifiedAt , COUNT(l.id) ) " +
                    "FROM Post p LEFT JOIN p.likes l " +
                    "GROUP BY p.postId"
    )
    List<PostSimpleResponseDto> getAllPostWithLikesCount();

}
