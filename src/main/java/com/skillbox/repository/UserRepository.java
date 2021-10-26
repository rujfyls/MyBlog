package com.skillbox.repository;

import com.skillbox.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select count(p) from Post p where moderationStatus = 'NEW'")
    Integer getModerationCount();

    @Query("SELECT u FROM User u WHERE email = :email")
    User getUserByEmail(String email);
}
