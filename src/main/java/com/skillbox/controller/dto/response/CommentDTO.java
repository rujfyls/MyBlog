package com.skillbox.controller.dto.response;

import com.skillbox.entity.Comment;
import com.skillbox.entity.User;

import java.time.ZoneOffset;

public class CommentDTO {

     private Integer id;

     private Long timestamp;

     private String text;

     private UserForCommentDTO user;

     public CommentDTO(Comment comment, User user) {
          this.id = comment.getCommentId();
          this.timestamp = comment.getTime().toEpochSecond(ZoneOffset.UTC);
          this.text = comment.getText();
          this.user = new UserForCommentDTO(user);
     }
}
