package com.skillbox.entity;

import javax.persistence.*;

@Entity
@Table(name = "tag2post")
public class TagForPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer tagForPostId;


    public TagForPost() {
    }

    public Integer getTagForPostId() {
        return tagForPostId;
    }

    public void setTagForPostId(Integer tagForPostId) {
        this.tagForPostId = tagForPostId;
    }
}
