package com.skillbox.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.skillbox.controller.dto.request.EnteredDataForEditPasswordRequestDTO;
import lombok.Data;

@Data
public class ErrorsEditPasswordDTO {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String code;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String password;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String captcha;

    public ErrorsEditPasswordDTO(EnteredDataForEditPasswordRequestDTO enteredDataForEditPassword) {
        this.code = enteredDataForEditPassword.getCode();
        this.password = enteredDataForEditPassword.getPassword();
        this.captcha = enteredDataForEditPassword.getCaptcha();
    }
}
