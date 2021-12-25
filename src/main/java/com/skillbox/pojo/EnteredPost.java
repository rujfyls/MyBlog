package com.skillbox.pojo;

import lombok.Data;

@Data
public class EnteredPost {

    private String title;

    private String text;

    public Boolean checkingForErrors() {
        return title.isEmpty() && text.isEmpty();
    }
}
