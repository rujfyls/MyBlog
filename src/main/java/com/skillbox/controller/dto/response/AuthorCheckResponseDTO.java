package com.skillbox.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class AuthorCheckResponseDTO {

    private Boolean result;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private UserDTO user;
}
