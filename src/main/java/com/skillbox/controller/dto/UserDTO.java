package com.skillbox.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.entity.User;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getModeration() {
        return moderation;
    }

    public void setModeration(Boolean moderation) {
        this.moderation = moderation;
    }

    public Integer getModerationCount() {
        return moderationCount;
    }

    public void setModerationCount(Integer moderationCount) {
        this.moderationCount = moderationCount;
    }

    public Boolean getSettings() {
        return settings;
    }

    public void setSettings(Boolean settings) {
        this.settings = settings;
    }
}
