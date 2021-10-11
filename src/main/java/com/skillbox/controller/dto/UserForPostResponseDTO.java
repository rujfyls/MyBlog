package com.skillbox.controller.dto;

import com.skillbox.entity.User;

public class UserForPostResponseDTO {

    private Integer id;

    private String name;

    public UserForPostResponseDTO(User user) {
        this.id = user.getUserId();
        this.name = user.getName();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
