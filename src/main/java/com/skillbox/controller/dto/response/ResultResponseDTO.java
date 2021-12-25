package com.skillbox.controller.dto.response;

import lombok.Data;

@Data
public class ResultResponseDTO {

    private Boolean result;

    public ResultResponseDTO(Boolean result) {
        this.result = result;
    }
}
