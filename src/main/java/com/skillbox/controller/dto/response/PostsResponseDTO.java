package com.skillbox.controller.dto.response;

import com.skillbox.entity.Post;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostsResponseDTO {

    private Integer count;

    private List<PostDTO> posts;

    public PostsResponseDTO(Integer count, List<Post> posts) {
        this.count = count;
        this.posts = posts.stream().map(PostDTO::new).collect(Collectors.toList());
    }
}
