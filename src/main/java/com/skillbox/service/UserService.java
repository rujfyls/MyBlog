package com.skillbox.service;

import com.skillbox.entity.User;
import com.skillbox.exceptions.UserNotFoundException;
import com.skillbox.pojo.EnteredUser;
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

    public void save(String name, String email, String password, String captchaSecret) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setIsModerator((short) 0);
        user.setCode(captchaSecret);

        userRepository.save(user);
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
