package com.skillbox.controller.dto.response;

import lombok.Data;
import lombok.NonNull;

@Data
public class CaptchaResponseDTO {

    @NonNull
    private String secret;

    @NonNull
    private String image;
}
