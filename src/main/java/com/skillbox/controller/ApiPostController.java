package com.skillbox.controller;

import com.skillbox.controller.dto.PostsResponseDTO;
import com.skillbox.controller.dto.TagResponseDTO;
import com.skillbox.service.PostService;
import com.skillbox.service.TagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiPostController {

    private final PostService postService;
    private final TagService tagService;

    public ApiPostController(PostService postService, TagService tagService) {
        this.postService = postService;

        this.tagService = tagService;
    }

    @GetMapping("/post")
    public PostsResponseDTO post(@RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset,
                                 @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
                                 @RequestParam(value = "mode", defaultValue = "recent", required = false) String mode) {

        return new PostsResponseDTO(postService.getCountAllActivePosts(), postService.findAllActivePosts(offset, limit, mode));
    }

    @GetMapping("/tag")
    public TagResponseDTO tag() {
        return new TagResponseDTO(tagService.getWeightAllTags(postService.getCountAllPosts()));
    }
}
