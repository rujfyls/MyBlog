package com.skillbox.pojo;

import lombok.Data;

@Data
public class EnteredDataForEditPassword {

    private String code;

    private String password;

    private String captcha;

    public Boolean checkingForErrors() {
        return code.isEmpty() && password.isEmpty() && captcha.isEmpty();
    }
}
