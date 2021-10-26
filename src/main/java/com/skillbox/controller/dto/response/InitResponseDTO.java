package com.skillbox.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class InitResponseDTO {

    @Value("${blog.title}")
    @JsonProperty("title")
    private String title;

    @Value("${blog.subtitle}")
    @JsonProperty("subtitle")
    private String subtitle;

    @Value("${blog.phone}")
    @JsonProperty("phone")
    private String phone;

    @Value("${blog.email}")
    @JsonProperty("email")
    private String email;

    @Value("${blog.copyright}")
    @JsonProperty("copyright")
    private String copyright;

    @Value("${blog.copyrightFrom}")
    @JsonProperty("copyrightFrom")
    private String copyrightFrom;
}
