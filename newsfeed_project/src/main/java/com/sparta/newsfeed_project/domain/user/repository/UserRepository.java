package com.sparta.newsfeed_project.domain.user.repository;

import com.sparta.newsfeed_project.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
