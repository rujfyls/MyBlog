package com.skillbox.pojo;

import lombok.Data;

@Data
public class EnteredDataForEditProfile {

    private String name = "";

    private String email = "";

    private String password = "";

    private String photo = "";

    public Boolean checkingEnteredData() {
        return name.isEmpty() && email.isEmpty() && password.isEmpty() && photo.isEmpty();
    }
}
