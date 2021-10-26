package com.skillbox.controller.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class TagResponseDTO {

    private List<TagDTO> tags = new ArrayList<>();

    public TagResponseDTO(Map<String, Double> listOfWeightsAllTags) {
        listOfWeightsAllTags.forEach((name, weight) -> tags.add(new TagDTO(name, weight)));
    }
}
