package com.skillbox.pojo;

import lombok.Data;

@Data
public class EnteredUser {

    private String email;

    private String name;

    private String password;

    private String captcha;

    public Boolean checkingForErrors() {
        return email.isEmpty() && name.isEmpty() && password.isEmpty() && captcha.isEmpty();
    }
}