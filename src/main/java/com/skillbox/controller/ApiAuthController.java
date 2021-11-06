package com.skillbox.controller;

import com.github.cage.GCage;
import com.github.cage.YCage;
import com.skillbox.controller.dto.request.LoginRequestDTO;
import com.skillbox.controller.dto.request.UserRequestDTO;
import com.skillbox.controller.dto.response.*;
import com.skillbox.entity.User;
import com.skillbox.pojo.EnteredUser;
import com.skillbox.service.CaptchaService;
import com.skillbox.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final UserService userService;
    private final CaptchaService captchaService;

    public ApiAuthController(UserService userService,
                             CaptchaService captchaService) {
        this.userService = userService;
        this.captchaService = captchaService;
    }

    @GetMapping("/check")
    public AuthorCheckResponseDTO authorCheck(Principal principal) {
        AuthorCheckResponseDTO author = new AuthorCheckResponseDTO();
        if (principal == null) {
            author.setResult(false);
            return author;
        }
        author.setResult(true);
        author.setUser(new UserDTO(userService.getUserByEmail(principal.getName()), userService.getModerationCount()));

        return author;
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
        EnteredUser enteredUser = userService.checkingEnteredData(userRequestDTO.getName(), userRequestDTO.getEmail(),
                userRequestDTO.getPassword(),
                captchaService.checkingCaptcha(userRequestDTO.getCaptcha(), userRequestDTO.getCaptchaSecret()));

        if (enteredUser.checkingForErrors()) {
            userService.save(userRequestDTO.getName(),
                    userRequestDTO.getEmail(),
                    userRequestDTO.getPassword(),
                    userRequestDTO.getCaptchaSecret());
        }
        return new RegisterResponseDTO(enteredUser);
    }

    @PostMapping("/login")
    public AuthorCheckResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {

        AuthorCheckResponseDTO author = new AuthorCheckResponseDTO();

        if (userService.getUserByEmail(loginRequestDTO.getEmail()) == null) {
            author.setResult(false);
            return author;
        }

        User user = userService.getAuthenticatedUser(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
        author.setResult(true);
        author.setUser(new UserDTO(user, userService.getModerationCount()));

        return author;
    }

    @GetMapping("/logout")
    public LogoutResponseDTO logout() {
        SecurityContextHolder.clearContext();
        return new LogoutResponseDTO();
    }
}
