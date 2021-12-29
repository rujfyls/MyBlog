package com.skillbox.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.skillbox.controller.dto.request.EnteredPostRequestDTO;
import lombok.Data;

@Data
public class ErrorsAddPostDTO {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String title;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String text;

    public ErrorsAddPostDTO(EnteredPostRequestDTO enteredPost) {
        this.title = enteredPost.getTitle();
        this.text = enteredPost.getText();
    }
}
