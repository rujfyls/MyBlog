package com.skillbox.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tags")
@Data
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer tagId;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "tag2post",
            joinColumns = {@JoinColumn(name = "tag_id")},
            inverseJoinColumns = {@JoinColumn(name = "post_id")}
    )
    private final List<Post> posts = new ArrayList<>();

    public List<Post> getPosts() {
        return posts;
    }

    public void addPost(Post post) {
        posts.add(post);
    }

    public void removePost(Post post) {
        posts.remove(post);
    }
}
