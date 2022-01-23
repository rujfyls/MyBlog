package com.skillbox.controller;

import com.github.cage.GCage;
import com.github.cage.YCage;
import com.skillbox.controller.dto.request.*;
import com.skillbox.controller.dto.response.*;
import com.skillbox.entity.User;
import com.skillbox.service.CaptchaService;
import com.skillbox.service.EmailService;
import com.skillbox.service.PostService;
import com.skillbox.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<UserCheckResponseDTO> authorCheck(Principal principal) {
        UserCheckResponseDTO user = new UserCheckResponseDTO();
        if (principal == null) {
            user.setResult(false);
            return ResponseEntity.ok(user);
        }
        user.setResult(true);
        user.setUser(new UserDTO(userService.getUserByEmail(principal.getName()),
                postService.getCountPostsForModeration("new",
                        userService.getUserByEmail(principal.getName()).getUserId())));

        return ResponseEntity.ok(user);
    }

    @GetMapping("/captcha")
    public ResponseEntity<CaptchaResponseDTO> captcha() throws IOException {
        GCage gCage = new GCage();
        YCage yCage = new YCage();
        String code = gCage.getTokenGenerator().next().substring(0, 5);
        String secretCode = yCage.getTokenGenerator().next();
        String image = Base64.getEncoder().encodeToString(gCage.draw(code));

        captchaService.saveCaptcha(secretCode, code);

        return ResponseEntity.ok(new CaptchaResponseDTO(secretCode, "data:image/png;base64, " + image));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody UserRequestDTO userRequestDTO) {

        EnteredUserRequestDTO enteredUser =
                userService.checkingEnteredData(userRequestDTO.getName(), userRequestDTO.getEmail(),
                        userRequestDTO.getPassword(),
                        captchaService.checkingCaptcha(userRequestDTO.getCaptcha(), userRequestDTO.getCaptchaSecret()));

        if (enteredUser.checkingForErrors()) {
            userService.save(userRequestDTO.getName(),
                    userRequestDTO.getEmail(),
                    encoder.encode(userRequestDTO.getPassword()),
                    userRequestDTO.getCaptchaSecret());
        }
        return ResponseEntity.ok(new RegisterResponseDTO(enteredUser));
    }

    @PostMapping("/login")
    public ResponseEntity<UserCheckResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        UserCheckResponseDTO author = new UserCheckResponseDTO();
        User currentUser = userService.getUserByEmail(loginRequestDTO.getEmail());

        if (currentUser == null) {
            author.setResult(false);
            return ResponseEntity.ok(author);
        }

        if (!encoder.matches(loginRequestDTO.getPassword(), currentUser.getPassword())) {
            author.setResult(false);
            return ResponseEntity.ok(author);
        }

        User user = userService.getAuthenticatedUser(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
        author.setResult(true);
        author.setUser(new UserDTO(user, postService.getCountPostsForModeration("new", user.getUserId())));

        return ResponseEntity.ok(author);
    }

    @GetMapping("/logout")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<LogoutResponseDTO> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new LogoutResponseDTO());
    }

    @PostMapping("/restore")
    public ResponseEntity<ResultResponseDTO> restorePassword(@RequestBody RestorePasswordRequestDTO restorePassword) {
        User user = userService.getUserByEmail(restorePassword.getEmail());

        if (user != null) {
            String hash = DigestUtils.sha256Hex(user.getEmail());
            user.setCode(hash);
            userService.save(user);

            userService.addNewHashInStorage(hash);

            emailService.sendMail(user.getEmail(),
                    "Восстановление пароля",
                    "ссылка для восстановления пароля: " +
                            "\n https://feduncov-blog.herokuapp.com/login/change-password/" + hash);
            return ResponseEntity.ok(new ResultResponseDTO(true));
        }
        return ResponseEntity.ok(new ResultResponseDTO(false));
    }

    @PostMapping("/password")
    public ResponseEntity<EditPasswordResponseDTO> editPassword(@RequestBody EditPasswordRequestDTO editPasswordRequestDTO) {

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

        return ResponseEntity.ok(new EditPasswordResponseDTO(enteredData));
    }
}
