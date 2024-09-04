package com.sparta.newsfeed_project.domain.user.entity;

import com.sparta.newsfeed_project.domain.friend.entity.Friend;
import com.sparta.newsfeed_project.domain.post.entity.Post;
import com.sparta.newsfeed_project.domain.user.dto.request.SignupRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false, unique = true)
    private String phoneNumber;
    private String imageSrc = "defualt_image.jpg";
    private int isDeleted = 0;
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Post> posts;
    @OneToMany(mappedBy = "followingUser")
    private List<Friend> friends;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(SignupRequestDto requestDto, String password) {
        this.username = requestDto.getUsername();
        this.email = requestDto.getEmail();
        this.password = password;
        this.phoneNumber = requestDto.getPhoneNumber();
        if (requestDto.getImageSrc() != null) {
            imageSrc = requestDto.getImageSrc();
        }
    }

    public void addPost(Post post) {
        posts.add(post);
        post.setUser(this);
    }
}
