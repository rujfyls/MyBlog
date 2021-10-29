package com.skillbox.controller;

import com.github.cage.GCage;
import com.github.cage.YCage;
import com.skillbox.controller.dto.request.UserRequestDTO;
import com.skillbox.controller.dto.response.AuthorCheckResponseDTO;
import com.skillbox.controller.dto.response.CaptchaResponseDTO;
import com.skillbox.controller.dto.response.RegisterResponseDTO;
import com.skillbox.controller.dto.response.UserDTO;
import com.skillbox.pojo.EnteredUser;
import com.skillbox.service.CaptchaService;
import com.skillbox.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    public AuthorCheckResponseDTO authorCheck() {
        AuthorCheckResponseDTO author = new AuthorCheckResponseDTO();
        //тут необходима реализация проверки авторизованности пользователя, а пока для теста json:
        author.setResult(false);
//        author.setUser(new UserDTO(userService.getUserById(1), userService.getModerationCount()));
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
}
