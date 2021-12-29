package com.skillbox.controller.dto.request;

import lombok.Data;

@Data
public class EnteredDataForEditPasswordRequestDTO {

    private String code;

    private String password;

    private String captcha;

    public Boolean checkingForErrors() {
        return code.isEmpty() && password.isEmpty() && captcha.isEmpty();
    }
}
