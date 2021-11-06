package com.skillbox.service;

import com.skillbox.entity.User;
import com.skillbox.exceptions.UserNotFoundException;
import com.skillbox.pojo.EnteredUser;
import com.skillbox.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    public Integer getModerationCount() {
        return userRepository.getModerationCount();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("user with id=" + id + " not found"));
    }

    public void save(String name, String email, String password, String captchaSecret) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setIsModerator((short) 0);
        user.setCode(captchaSecret);

        userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public EnteredUser checkingEnteredData(String name,
                                           String email,
                                           String password,
                                           String captcha) {
        EnteredUser inputError = new EnteredUser();
        inputError.setEmail(checkingEmail(email));
        inputError.setName(checkingName(name));
        inputError.setPassword(checkingPassword(password));
        inputError.setCaptcha(captcha);

        return inputError;
    }

    public User getAuthenticatedUser(String email, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(auth);

        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) auth.getPrincipal();

        return userRepository.getUserByEmail(user.getUsername());
    }

    private String checkingName(String name) {
        if (name.trim().isEmpty()) {
            return "Имя не введено";
        } else {
            return "";
        }
    }

    private String checkingEmail(String email) {
        User user = userRepository.getUserByEmail(email);
        if (user != null) {
            return "Этот e-mail уже зарегистрирован";
        } else {
            return "";
        }
    }

    private String checkingPassword(String password) {
        if (password.trim().length() < 6) {
            return "Пароль короче 6-ти символов";
        } else {
            return "";
        }
    }
}
