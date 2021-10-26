package com.skillbox.controller.dto.response;

import com.skillbox.entity.User;
import lombok.Data;

@Data
public class UserForCommentDTO {

    private Integer id;

    private String name;

    private String photo;

    public UserForCommentDTO(User user) {
        this.id = user.getUserId();
        this.name = user.getName();
        this.photo = user.getPhoto();
    }
}
