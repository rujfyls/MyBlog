package com.skillbox.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.skillbox.controller.dto.request.EnteredPostRequestDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddingAndEditingPostResponseDTO {

    private Boolean result;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ErrorsAddPostDTO errors;

    public AddingAndEditingPostResponseDTO(EnteredPostRequestDTO enteredPost) {
        if (enteredPost.getTitle().isEmpty() &&
                enteredPost.getText().isEmpty()) {
            this.result = true;
        } else {
            this.result = false;
            this.errors = new ErrorsAddPostDTO(enteredPost);
        }
    }
}
