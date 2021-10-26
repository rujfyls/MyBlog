package com.skillbox.entity;

import com.skillbox.controller.dto.request.UserRequestDTO;
import com.skillbox.pojo.EnteredUser;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer userId;

    @Column(name = "is_moderator", columnDefinition = "smallint", nullable = false)
    private Short isModerator;

    @Column(name = "reg_time", nullable = false)
    private LocalDateTime regTime = LocalDateTime.now();

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "code")
    private String code;

    @Column(name = "photo", columnDefinition = "TEXT")
    private String photo;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<Vote> listOfVotes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<Comment> comments = new ArrayList<>();

    public void addPost(Post post) {
        posts.add(post);
    }

    public void removePost(Post post) {
        posts.remove(post);
    }

    public List<Vote> getListOfVotes() {
        return listOfVotes;
    }

    public void addVote(Vote vote) {
        listOfVotes.add(vote);
    }

    public void removeVote(Vote vote) {
        listOfVotes.remove(vote);
    }

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
