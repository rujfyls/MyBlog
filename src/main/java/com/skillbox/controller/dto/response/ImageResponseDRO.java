package com.skillbox.controller.dto.response;

import lombok.Data;

@Data
public class ImageResponseDRO {

    private Boolean result;

    private ErrorsImageDTO errors;

    public ImageResponseDRO(String message) {
        this.result = false;
        errors = new ErrorsImageDTO(message);
    }

    @Override
    public String toString() {
        return "{" +
                "result: " + result +
                ", errors: " + errors +
                '}';
    }
}
