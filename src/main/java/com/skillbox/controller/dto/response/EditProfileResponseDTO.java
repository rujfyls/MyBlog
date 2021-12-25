package com.skillbox.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.skillbox.pojo.EnteredDataForEditProfile;
import lombok.Data;

@Data
public class EditProfileResponseDTO {

    private Boolean result;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ErrorsEditProfileDTO errors;

    public EditProfileResponseDTO(EnteredDataForEditProfile enteredData) {
        if (enteredData.getEmail().isEmpty() &&
                enteredData.getName().isEmpty() &&
                enteredData.getPassword().isEmpty() &&
                enteredData.getPhoto().isEmpty()) {
            this.result = true;
        } else {
            this.result = false;
            this.errors = new ErrorsEditProfileDTO(enteredData);
        }
    }
}
