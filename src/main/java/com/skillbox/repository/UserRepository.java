package com.skillbox.repository;

import com.skillbox.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(nativeQuery = true, value = "select count(*) from posts where moderation_status='NEW'")
    Integer getModerationCount();
}
