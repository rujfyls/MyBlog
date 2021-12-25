package com.skillbox.controller.dto.request;

import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@ToString
public class ProfileRequestDTO implements Serializable {

    private String name;

    private String email;

    private String password;

    private Short removePhoto;
}
