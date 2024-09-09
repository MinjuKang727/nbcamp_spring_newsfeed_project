package com.sparta.newsfeed_project.domain.post.entity;

import com.sparta.newsfeed_project.domain.comment.entity.Comment;
import com.sparta.newsfeed_project.domain.common.entity.Timestamped;
import com.sparta.newsfeed_project.domain.like.entity.Like;
import com.sparta.newsfeed_project.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post extends Timestamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    private String title;
    private String content;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Like> likes;

    public Post (String title, String content, User postUser){
        this.title =title;
        this.content = content;
        this.user = postUser;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
