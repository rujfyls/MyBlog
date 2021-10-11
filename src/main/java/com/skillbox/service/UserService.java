package com.skillbox.service;

import com.skillbox.entity.User;
import com.skillbox.exceptions.UserNotFoundException;
import com.skillbox.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Integer getModerationCount() {
        return userRepository.getModerationCount();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("user with id=" + id + " not found"));
    }
}
