package com.skillbox.controller.dto.response;

import lombok.Data;

@Data
public class ErrorsImageDTO {

    private String image;

    public ErrorsImageDTO(String message) {
        this.image = message;
    }

    @Override
    public String toString() {
        return "{" +
                "image: \"" + image + '\"' +
                '}';
    }
}
