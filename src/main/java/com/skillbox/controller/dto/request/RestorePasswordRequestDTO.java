package com.skillbox.controller.dto.request;

import lombok.Data;

import java.io.Serializable;
@Data
public class RestorePasswordRequestDTO implements Serializable {

    private String email;
}
