package com.skillbox.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class CaptchaResponseDTO {

    private String secret;

    private String image;
}
