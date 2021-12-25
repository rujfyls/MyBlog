package com.skillbox.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LikeOrDislikeRequestDTO {

    @JsonProperty("post_id")
    private Integer postId;
}
