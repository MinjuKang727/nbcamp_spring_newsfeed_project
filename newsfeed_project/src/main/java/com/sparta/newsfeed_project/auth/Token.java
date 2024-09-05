package com.sparta.newsfeed_project.auth;

import com.sparta.newsfeed_project.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    @Enumerated(EnumType.STRING)
    private final String BEARER = "BEARER";
    private boolean expired;
    private boolean revoked;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
