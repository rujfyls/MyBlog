package com.skillbox.controller.dto;

import com.skillbox.entity.Post;
import com.skillbox.entity.Vote;

import java.time.ZoneOffset;

public class PostDTO {

    private Integer id;

    private Long timestamp;

    private UserForPostResponseDTO user;

    private String title;

    private String announce;

    private Integer likeCount;

    private Integer dislikeCount;

    private Integer commentCount;

    private Integer viewCount;

    public PostDTO(Post post) {
        this.id = post.getPostId();
        this.timestamp = post.getTime().toEpochSecond(ZoneOffset.UTC);
        this.user = new UserForPostResponseDTO(post.getUser());
        this.title = post.getTitle();
        this.announce = post.getText().substring(0, 145).concat("...");
        this.likeCount = post.getListOfVotes().stream().filter(v -> v.getValue() > 0).mapToInt(Vote::getValue).sum();
        this.dislikeCount = post.getListOfVotes().stream()
                .filter(v -> v.getValue() < 0).mapToInt(Vote::getValue).map(Math::abs).sum();
        this.commentCount = post.getComments().size();
        this.viewCount = post.getViewCount();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public UserForPostResponseDTO getUser() {
        return user;
    }

    public void setUser(UserForPostResponseDTO user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnnounce() {
        return announce;
    }

    public void setAnnounce(String announce) {
        this.announce = announce;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(Integer dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }
}
