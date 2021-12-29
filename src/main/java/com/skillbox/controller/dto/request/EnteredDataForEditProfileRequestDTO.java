package com.skillbox.controller.dto.request;

import lombok.Data;

@Data
public class EnteredDataForEditProfileRequestDTO {

    private String name = "";

    private String email = "";

    private String password = "";

    private String photo = "";

    public Boolean checkingEnteredData() {
        return name.isEmpty() && email.isEmpty() && password.isEmpty() && photo.isEmpty();
    }
}
