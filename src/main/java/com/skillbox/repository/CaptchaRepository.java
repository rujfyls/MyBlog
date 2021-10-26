package com.skillbox.repository;

import com.skillbox.entity.Captcha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CaptchaRepository extends JpaRepository<Captcha, Integer> {

    @Query("SELECT c FROM Captcha c WHERE c.secretCode = :secretCode")
    Captcha findCaptchaBySecretCode(String secretCode);
}
