package com.skillbox.controller.dto.response;

import lombok.Data;
import lombok.NonNull;

@Data
public class TagDTO {

    @NonNull
    private String name;

    @NonNull
    private Double weight;
}
