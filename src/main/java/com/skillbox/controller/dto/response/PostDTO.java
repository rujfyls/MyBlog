package com.skillbox.controller.dto.response;

import com.skillbox.entity.Post;
import com.skillbox.entity.Vote;
import lombok.Data;

import java.time.ZoneOffset;

@Data
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
        if (post.getText().length() > 145) {
            this.announce = post.getText().substring(0, 145).concat("...");
        } else {
            this.announce = post.getText();
        }
        this.likeCount = post.getListOfVotes().stream().filter(v -> v.getValue() > 0).mapToInt(Vote::getValue).sum();
        this.dislikeCount = post.getListOfVotes().stream()
                .filter(v -> v.getValue() < 0).mapToInt(Vote::getValue).map(Math::abs).sum();
        this.commentCount = post.getComments().size();
        this.viewCount = post.getViewCount();
    }
}
