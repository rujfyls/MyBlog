package com.skillbox.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @JsonProperty("e_mail")
    private String email;

    private String password;
}
