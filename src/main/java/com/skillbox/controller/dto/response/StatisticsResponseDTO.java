package com.skillbox.controller.dto.response;

import lombok.Data;

@Data
public class StatisticsResponseDTO {

    private Integer postsCount;

    private Integer likesCount;

    private Integer dislikesCount;

    private Integer viewsCount;

    private Long firstPublication;
}
