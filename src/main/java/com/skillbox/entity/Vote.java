package com.skillbox.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "post_votes")
@Data
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer postVotesId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "time", nullable = false)
    private LocalDateTime time = LocalDateTime.now();

    @Column(name = "value", columnDefinition = "smallint", nullable = false)
    private Short value;
}
