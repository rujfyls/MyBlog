package com.skillbox.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EditPasswordRequestDTO {

    @JsonProperty("code")
    private String code;

    @JsonProperty("password")
    private String password;

    @JsonProperty("captcha")
    private String captcha;

    @JsonProperty("captcha_secret")
    private String captchaSecret;
}