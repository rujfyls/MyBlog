package com.skillbox.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class AuthorCheckResponseDTO {

    private Boolean result;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private UserDTO user;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
