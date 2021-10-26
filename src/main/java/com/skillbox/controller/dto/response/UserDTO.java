package com.skillbox.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.entity.User;
import lombok.Data;

@Data
public class UserDTO {

    @JsonProperty("id")
    private Integer userId;

    private String name;

    private String photo;

    private String email;

    private Boolean moderation;

    private Integer moderationCount;

    private Boolean settings;

    public UserDTO(User user, Integer moderationCount) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.photo = user.getPhoto();
        this.email = user.getEmail();
        if (user.getIsModerator() > 0) {
            this.moderation = true;
            this.settings = true;
            this.moderationCount = moderationCount;
        } else {
            this.moderation = false;
            this.settings = false;
            this.moderationCount = 0;
        }
    }
}
