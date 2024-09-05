package com.sparta.newsfeed_project.domain.post.entity;

import com.sparta.newsfeed_project.domain.comment.entity.Comment;
import com.sparta.newsfeed_project.domain.common.entity.Timestamped;
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
@Table(name="posts")
public class Post extends Timestamped {
    // 게시글
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private String title;
    private String content;


    // 게시글 작성자
    @Setter
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    // 댓글
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments;

    //생성자
    public Post (String title, String content, User postUser){
        this.title =title;
        this.content = content;
        this.user = postUser;
    }


    public void addComment(Comment comment, User commentUser) {
        this.user = commentUser;
        comments.add(comment);
        comment.setPost(this);
    }

    // 게시글 수정 메서드 여기에 입력된 값만 수정 가능합니다.
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
