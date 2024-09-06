package com.sparta.newsfeed_project.domain.user.entity;

import com.sparta.newsfeed_project.domain.friend.entity.Friend;
import com.sparta.newsfeed_project.domain.post.entity.Post;
import com.sparta.newsfeed_project.domain.user.dto.request.UserCreateRequestDto;
import com.sparta.newsfeed_project.domain.user.dto.request.UserUpdateRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Post> posts;
    @OneToMany(mappedBy = "followingUser")
    private List<Friend> friends;

    public User(String username, String email, String password, UserRole role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(UserCreateRequestDto requestDto, String password, UserRole role) {
        this.username = requestDto.getUsername();
        this.email = requestDto.getEmail();
        this.password = password;
        this.phoneNumber = requestDto.getPhoneNumber();
        if (requestDto.getImageSrc() != null) {
            imageSrc = requestDto.getImageSrc();
        }
        this.role = role;
    }

    public void updateUser(UserUpdateRequestDto requestDto, String password) {
        if (requestDto.getNewUsername() != null) {
            this.username = requestDto.getNewUsername();
        }
        if (requestDto.getNewEmail() != null) {
            this.email = requestDto.getNewEmail();
        }
        if (password != null) {
            this.password = password;
        }
        if (requestDto.getNewPhoneNumber() != null) {
            this.phoneNumber = requestDto.getNewPhoneNumber();
        }
        if (requestDto.getNewImageSrc() != null) {
            this.imageSrc = requestDto.getNewImageSrc();
        }
    }

    public void addPost(Post post) {
        posts.add(post);
        post.setUser(this);
    }

    public void delete() {
        this.isDeleted = 1;
    }
}
