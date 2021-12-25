package com.skillbox.controller.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class PostRequestDTO {

    private Long timestamp;

    private Short active;

    private String title;

    private List<String> tags;

    private String text;
}
