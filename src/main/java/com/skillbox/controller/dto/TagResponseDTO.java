package com.skillbox.controller.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TagResponseDTO {

    private List<TagDTO> tags = new ArrayList<>();

    public TagResponseDTO(Map<String, Double> listOfWeightsAllTags) {
        listOfWeightsAllTags.forEach((name, weight) -> tags.add(new TagDTO(name, weight)));
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }
}
