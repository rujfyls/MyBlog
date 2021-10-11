package com.skillbox.controller.dto;

import com.skillbox.entity.Post;

import java.util.List;
import java.util.stream.Collectors;

public class PostsResponseDTO {

    private Integer count;

    private List<PostDTO> posts;

    public PostsResponseDTO(Integer count, List<Post> posts) {
        this.count = count;
        this.posts = posts.stream().map(PostDTO::new).collect(Collectors.toList());
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<PostDTO> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDTO> posts) {
        this.posts = posts;
    }
}
