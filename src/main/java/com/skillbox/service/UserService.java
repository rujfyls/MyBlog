package com.skillbox.service;

import com.skillbox.entity.User;
import com.skillbox.exceptions.UserNotFoundException;
import com.skillbox.pojo.EnteredDataForEditPassword;
import com.skillbox.pojo.EnteredDataForEditProfile;
import com.skillbox.pojo.EnteredUser;
import com.skillbox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Value("${blog.hashForRestorePasswordDeletionIntervalInHours}")
    private String hashForRestorePasswordDeletionInterval;
    private static final Map<String, LocalTime> hashStorageForRestorePassword = new ConcurrentHashMap<>();

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
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

    public void save(User user) {
        userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
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

    public EnteredDataForEditProfile checkingEnteredDataForEditProfile(User user,
                                                                       String name,
                                                                       String email,
                                                                       String password,
                                                                       MultipartFile photo) {
        EnteredDataForEditProfile enteredData = new EnteredDataForEditProfile();
        enteredData.setName(checkingName(name));
        if (!user.getEmail().equals(email)) {
            enteredData.setEmail(checkingEmail(email));
        }
        if (password != null) {
            enteredData.setPassword(checkingPassword(password));
        }
        if (photo != null) {
            if (photo.getSize() > 5_242_880) {
                enteredData.setPhoto("Фото слишком большое, нужно не более 5 Мб");
            }
        }

        return enteredData;
    }

    public EnteredDataForEditPassword checkingEnteredDataForEditPassword(User user,
                                                                         String code,
                                                                         String password,
                                                                         String captcha) {
        EnteredDataForEditPassword enteredData = new EnteredDataForEditPassword();
        LocalTime hashCreationTime = hashStorageForRestorePassword.get(code);
        LocalTime time = LocalTime.now().minusHours(Long.parseLong(hashForRestorePasswordDeletionInterval));

        if (hashCreationTime == null || hashCreationTime.compareTo(time) < 0) {
            user.setCode(null);
            enteredData.setCode("Ссылка для восстановления пароля устарела. " +
                    "<a href=\"/login/restore-password\">Запросить ссылку снова</a>");
            hashStorageForRestorePassword.remove(code);
        } else {
            enteredData.setCode("");
        }

        if (password != null) {
            enteredData.setPassword(checkingPassword(password));
        }
        enteredData.setCaptcha(captcha);

        return enteredData;
    }

    public User getAuthenticatedUser(String email, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(auth);

        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) auth.getPrincipal();

        return userRepository.findUserByEmail(user.getUsername());
    }

    public List<Integer> getListUsersId() {
        return userRepository.findAll().stream().map(User::getUserId).collect(Collectors.toList());
    }

    public User getUserByCode(String code) {
        return userRepository.findUserByCode(code);
    }

    public void addNewHashInStorage(String hash) {
        hashStorageForRestorePassword.put(hash, LocalTime.now(ZoneId.systemDefault()));
    }

    private String checkingName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Имя не введено";
        } else {
            return "";
        }
    }

    private String checkingEmail(String email) {
        User user = userRepository.findUserByEmail(email);
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
