package com.skillbox.controller.dto.request;

import lombok.Data;

@Data
public class EnteredPostRequestDTO {

    private String title;

    private String text;

    public Boolean checkingForErrors() {
        return title.isEmpty() && text.isEmpty();
    }
}
