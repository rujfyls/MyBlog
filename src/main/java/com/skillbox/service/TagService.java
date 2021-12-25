package com.skillbox.service;

import com.skillbox.entity.Tag;
import com.skillbox.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.*;

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
                listOfTagsWithWeight.replace(name, weight, Math.rint((weight * (1 / maxWeight)) * 100) / 100));

        return listOfTagsWithWeight;
    }

    public List<Tag> getTagsOrSave(List<String> tagNames) {
        List<Tag> tags = new ArrayList<>();

        tagNames.stream().map(String::toUpperCase).forEach(name -> {
            Tag findTag = tagRepository.findByName(name);
            if (findTag == null) {
                Tag tag = new Tag();
                tag.setName(name);
                tagRepository.save(tag);

                tags.add(tag);
            } else {
                tags.add(findTag);
            }
        });

        return tags;
    }
}
