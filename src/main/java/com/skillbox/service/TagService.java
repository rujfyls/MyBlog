package com.skillbox.service;

import com.skillbox.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Map<String, Double> getWeightAllTags(Integer countAllPosts) {
        Map<String, Double> listOfTagsWithWeight = new HashMap<>();

        tagRepository.findAll().forEach(tag ->
                listOfTagsWithWeight.put(tag.getName(), (double) tag.getPosts().size() / countAllPosts)
        );

        double maxWeight = Collections.max(listOfTagsWithWeight.values());

        listOfTagsWithWeight.forEach((name, weight) ->
                listOfTagsWithWeight.replace(name, weight, Math.rint((weight * maxWeight) * 100) / 100));

        return listOfTagsWithWeight;
    }
}
