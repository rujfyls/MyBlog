package com.skillbox.controller.dto.response;

import com.skillbox.entity.User;
import lombok.Data;

@Data
public class UserForPostResponseDTO {

    private Integer id;

    private String name;

    public UserForPostResponseDTO(User user) {
        this.id = user.getUserId();
        this.name = user.getName();
    }
}
