package com.skillbox.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class CommentResponseDTO {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Integer commentId;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean result;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ErrorsCommentDTO errors;

    public void setErrors(ErrorsCommentDTO errors) {
        this.errors = errors;
        this.result = false;
    }
}
