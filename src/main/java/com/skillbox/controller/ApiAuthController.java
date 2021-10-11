package com.skillbox.controller;

import com.skillbox.controller.dto.AuthorCheckResponseDTO;
import com.skillbox.controller.dto.UserDTO;
import com.skillbox.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final UserService userService;

    public ApiAuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/check")
    public AuthorCheckResponseDTO authorCheck() {
        AuthorCheckResponseDTO author = new AuthorCheckResponseDTO();
        //тут необходима реализация проверки авторизованности пользователя, а пока для теста json:
        author.setResult(true);
        author.setUser(new UserDTO(userService.getUserById(1), userService.getModerationCount()));
        return author;
    }
}
