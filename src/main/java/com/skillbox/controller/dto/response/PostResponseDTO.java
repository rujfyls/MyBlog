package com.skillbox.controller.dto.response;

import com.skillbox.entity.Post;
import com.skillbox.entity.Tag;
import com.skillbox.entity.Vote;
import lombok.Data;

import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostResponseDTO {

    private Integer id;

    private Long timestamp;

    private Boolean active;

    private UserForPostResponseDTO user;

    private String title;

    private String text;

    private Integer likeCount;

    private Integer dislikeCount;

    private Integer viewCount;

    private List<CommentDTO> comments;

    private List<String> tags;

    public PostResponseDTO(Post post) {
        this.id = post.getPostId();
        this.timestamp = post.getTime().toEpochSecond(ZoneOffset.UTC);
        this.active = post.getIsActive() > 0;
        this.user = new UserForPostResponseDTO(post.getUser());
        this.title = post.getTitle();
        this.text = post.getText();
        this.likeCount = post.getListOfVotes().stream().filter(v -> v.getValue() > 0).mapToInt(Vote::getValue).sum();
        this.dislikeCount = post.getListOfVotes().stream()
                .filter(v -> v.getValue() < 0).mapToInt(Vote::getValue).map(Math::abs).sum();
        this.viewCount = post.getViewCount();
        this.comments = post.getComments().stream().map(comment ->
                new CommentDTO(comment, comment.getUser())).collect(Collectors.toList());
        this.tags = post.getTags().stream().map(Tag::getName).collect(Collectors.toList());
    }
}
