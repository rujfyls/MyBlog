package com.skillbox.repository;

import com.skillbox.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE email = :email")
    User findUserByEmail(String email);

    @Query("SELECT u FROM User u WHERE code = :code")
    User findUserByCode(String code);
}
