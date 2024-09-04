package com.sparta.newsfeed_project.domain.friend.entity;

import com.sparta.newsfeed_project.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followingUserId", nullable = false)
    private User followingUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followedUserId", nullable = false)
    private User followedUser;

    private boolean accepted;

    public Friend(User followingUser, User followedUser) {
        this.followingUser = followingUser;
        this.followedUser = followedUser;
        this.accepted = false;
    }

    public void acceptFollow() {
        this.accepted = true;
    }
}
