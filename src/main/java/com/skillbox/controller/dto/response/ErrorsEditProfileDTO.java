package com.skillbox.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.skillbox.controller.dto.request.EnteredDataForEditProfileRequestDTO;
import lombok.Data;

@Data
public class ErrorsEditProfileDTO {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String name;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String email;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String password;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String photo;

    public ErrorsEditProfileDTO(EnteredDataForEditProfileRequestDTO enteredData) {
        this.name = enteredData.getName();
        this.email = enteredData.getEmail();
        this.password = enteredData.getPassword();
        this.photo = enteredData.getPhoto();
    }
}
