package com.skillbox.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "captcha_codes")
@Data
public class Captcha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer captchaId;

    @Column(name = "time", nullable = false)
    private LocalDateTime time = LocalDateTime.now();

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "secret_code", nullable = false)
    private String secretCode;
}
