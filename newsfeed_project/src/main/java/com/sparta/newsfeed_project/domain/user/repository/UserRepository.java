package com.sparta.newsfeed_project.domain.user.repository;

import com.sparta.newsfeed_project.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByEmailAndIsDeleted(String email, int isDeleted);
    Optional<User> findUserByIdAndIsDeleted(long id, int isDeleted);
    Long countAllByEmail(String email);
    Long countAllByPhoneNumberAndIsDeleted(String phoneNumber, int isDeleted);
}
