package com.skillbox.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.skillbox.pojo.EnteredPost;
import lombok.Data;

@Data
public class ErrorsAddPostDTO {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String title;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String text;

    public ErrorsAddPostDTO(EnteredPost enteredPost) {
        this.title = enteredPost.getTitle();
        this.text = enteredPost.getText();
    }
}
