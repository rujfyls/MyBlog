package com.skillbox.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "captcha_codes")
public class Captcha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer captchaId;

    @Column(name = "time", nullable = false)
    private LocalDateTime time = LocalDateTime.now();

    @Column(name = "code", columnDefinition = "smallint", nullable = false)
    private Short code;

    @Column(name = "secret_code", columnDefinition = "smallint", nullable = false)
    private Short secretCode;

    public Captcha() {
    }

    public Integer getCaptchaId() {
        return captchaId;
    }

    public void setCaptchaId(Integer captchaId) {
        this.captchaId = captchaId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Short getCode() {
        return code;
    }

    public void setCode(Short code) {
        this.code = code;
    }

    public Short getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(Short secretCode) {
        this.secretCode = secretCode;
    }
}
