package com.skillbox.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.skillbox.pojo.EnteredDataForEditPassword;
import lombok.Data;

@Data
public class EditPasswordResponseDTO {

    private Boolean result;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ErrorsEditPasswordDTO errors;

    public EditPasswordResponseDTO(EnteredDataForEditPassword enteredDataForEditPassword) {
        if (enteredDataForEditPassword.getPassword().isEmpty() &&
                enteredDataForEditPassword.getCaptcha().isEmpty() &&
                enteredDataForEditPassword.getCode().isEmpty()) {
            this.result = true;
        } else {
            this.result = false;
            this.errors = new ErrorsEditPasswordDTO(enteredDataForEditPassword);
        }
    }
}
