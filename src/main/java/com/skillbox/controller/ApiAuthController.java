package com.skillbox.controller;

import com.github.cage.GCage;
import com.github.cage.YCage;
import com.skillbox.controller.dto.request.EditPasswordRequestDTO;
import com.skillbox.controller.dto.request.LoginRequestDTO;
import com.skillbox.controller.dto.request.RestorePasswordRequestDTO;
import com.skillbox.controller.dto.request.UserRequestDTO;
import com.skillbox.controller.dto.response.*;
import com.skillbox.entity.User;
import com.skillbox.controller.dto.request.EnteredDataForEditPasswordRequestDTO;
import com.skillbox.controller.dto.request.EnteredUserRequestDTO;
import com.skillbox.service.CaptchaService;
import com.skillbox.service.EmailService;
import com.skillbox.service.PostService;
import com.skillbox.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    private final UserService userService;
    private final CaptchaService captchaService;
    private final EmailService emailService;
    private final PostService postService;

    public ApiAuthController(UserService userService,
                             CaptchaService captchaService,
                             EmailService emailService,
                             PostService postService) {
        this.userService = userService;
        this.captchaService = captchaService;
        this.emailService = emailService;
        this.postService = postService;
    }

    @GetMapping("/check")
    public UserCheckResponseDTO authorCheck(Principal principal) {
        UserCheckResponseDTO user = new UserCheckResponseDTO();
        if (principal == null) {
            user.setResult(false);
            return user;
        }
        user.setResult(true);
        user.setUser(new UserDTO(userService.getUserByEmail(principal.getName()),
                postService.getCountPostsForModeration("new",
                        userService.getUserByEmail(principal.getName()).getUserId())));

        return user;
    }

    @GetMapping("/captcha")
    public CaptchaResponseDTO captcha() throws IOException {
        GCage gCage = new GCage();
        YCage yCage = new YCage();
        String code = gCage.getTokenGenerator().next().substring(0, 5);
        String secretCode = yCage.getTokenGenerator().next();
        String image = Base64.getEncoder().encodeToString(gCage.draw(code));

        captchaService.saveCaptcha(secretCode, code);

        return new CaptchaResponseDTO(secretCode, "data:image/png;base64, " + image);
    }

    @PostMapping("/register")
    public RegisterResponseDTO register(@RequestBody UserRequestDTO userRequestDTO) {
        EnteredUserRequestDTO enteredUser = userService.checkingEnteredData(userRequestDTO.getName(), userRequestDTO.getEmail(),
                userRequestDTO.getPassword(),
                captchaService.checkingCaptcha(userRequestDTO.getCaptcha(), userRequestDTO.getCaptchaSecret()));

        if (enteredUser.checkingForErrors()) {
            userService.save(userRequestDTO.getName(),
                    userRequestDTO.getEmail(),
                    encoder.encode(userRequestDTO.getPassword()),
                    userRequestDTO.getCaptchaSecret());
        }
        return new RegisterResponseDTO(enteredUser);
    }

    @PostMapping("/login")
    public UserCheckResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
        UserCheckResponseDTO author = new UserCheckResponseDTO();

        if (userService.getUserByEmail(loginRequestDTO.getEmail()) == null) {
            author.setResult(false);
            return author;
        }

        User user = userService.getAuthenticatedUser(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
        author.setResult(true);
        author.setUser(new UserDTO(user, postService.getCountPostsForModeration("new", user.getUserId())));

        return author;
    }

    @GetMapping("/logout")
    @PreAuthorize("hasAuthority('user:write')")
    public LogoutResponseDTO logout() {
        SecurityContextHolder.clearContext();
        return new LogoutResponseDTO();
    }

    @PostMapping("/restore")
    public ResultResponseDTO restorePassword(@RequestBody RestorePasswordRequestDTO restorePassword) {
        User user = userService.getUserByEmail(restorePassword.getEmail());

        if (user != null) {
            String hash = DigestUtils.sha256Hex(user.getEmail());
            user.setCode(hash);
            userService.save(user);

            userService.addNewHashInStorage(hash);

            emailService.sendMail(user.getEmail(),
                    "Восстановление пароля",
                    "ссылка для восстановления пароля: \n https://feduncov-blog.herokuapp.com/login/change-password/" + hash);
            return new ResultResponseDTO(true);
        }
        return new ResultResponseDTO(false);
    }

    @PostMapping("/password")
    public EditPasswordResponseDTO editPassword(@RequestBody EditPasswordRequestDTO editPasswordRequestDTO) {

        User user = userService.getUserByCode(editPasswordRequestDTO.getCode());
        EnteredDataForEditPasswordRequestDTO enteredData = userService.checkingEnteredDataForEditPassword(
                user,
                editPasswordRequestDTO.getCode(),
                editPasswordRequestDTO.getPassword(),
                captchaService.checkingCaptcha(editPasswordRequestDTO.getCaptcha(), editPasswordRequestDTO.getCaptchaSecret())
        );

        if (enteredData.checkingForErrors()) {
            user.setPassword(encoder.encode(editPasswordRequestDTO.getPassword()));
            userService.save(user);
        }

        return new EditPasswordResponseDTO(enteredData);
    }
}
