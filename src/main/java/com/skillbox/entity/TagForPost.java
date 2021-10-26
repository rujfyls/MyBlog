package com.skillbox.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "tag2post")
@Data
public class TagForPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer tagForPostId;
}
