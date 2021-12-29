package com.skillbox.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.skillbox.controller.dto.request.EnteredUserRequestDTO;
import lombok.Data;

@Data
public class RegisterResponseDTO {

    private Boolean result;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ErrorsRegisterDTO errors;

    public RegisterResponseDTO(EnteredUserRequestDTO enteredUser) {
        if (enteredUser.getCaptcha().isEmpty() &&
                enteredUser.getEmail().isEmpty() &&
                enteredUser.getName().isEmpty() &&
                enteredUser.getPassword().isEmpty()) {
            this.result = true;
        } else {
            this.result = false;
            this.errors = new ErrorsRegisterDTO(enteredUser);
        }
    }
}
