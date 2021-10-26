package com.skillbox.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post_comments")
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer commentId;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "time", nullable = false)
    private LocalDateTime time = LocalDateTime.now();

    @Column(name = "text", columnDefinition = "TEXT", nullable = false)
    private String text;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private final List<Comment> comments = new ArrayList<>();

    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
    }
}
