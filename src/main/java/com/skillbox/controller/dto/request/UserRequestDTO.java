package com.skillbox.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserRequestDTO {

    @JsonProperty("e_mail")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("name")
    private String name;

    @JsonProperty("captcha")
    private String captcha;

    @JsonProperty("captcha_secret")
    private String captchaSecret;
}
