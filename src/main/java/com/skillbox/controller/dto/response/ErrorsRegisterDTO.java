package com.skillbox.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.skillbox.controller.dto.request.EnteredUserRequestDTO;
import lombok.Data;

@Data
public class ErrorsRegisterDTO {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String email;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String password;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String name;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String captcha;

    public ErrorsRegisterDTO(EnteredUserRequestDTO userInputError) {
        this.email = userInputError.getEmail();
        this.password = userInputError.getPassword();
        this.name = userInputError.getName();
        this.captcha = userInputError.getCaptcha();
    }
}
