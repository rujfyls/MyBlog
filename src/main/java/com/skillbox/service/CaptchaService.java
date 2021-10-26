package com.skillbox.service;

import com.skillbox.entity.Captcha;
import com.skillbox.repository.CaptchaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CaptchaService {

    @Value("${blog.captchaDeletionIntervalInHours}")
    private String interval;

    private final CaptchaRepository captchaRepository;

    public CaptchaService(CaptchaRepository captchaRepository) {
        this.captchaRepository = captchaRepository;
    }

    public void saveCaptcha(String secretCode, String code) {
        Captcha captcha = new Captcha();
        captcha.setCode(code);
        captcha.setSecretCode(secretCode);

        deleteOldData();
        captchaRepository.save(captcha);
    }

    public String checkingCaptcha(String captcha, String captchaSecret) {
        deleteOldData();
        Captcha findCaptcha = captchaRepository.findCaptchaBySecretCode(captchaSecret);

        if (findCaptcha == null) {
            return "Истек срок действия капчи";
        }
        if (!findCaptcha.getCode().equals(captcha)) {
            return "Код с картинки введён неверно";
        } else {
            return "";
        }
    }

    private void deleteOldData() {
        LocalDateTime time = LocalDateTime.now().minusHours(Long.parseLong(interval));

        captchaRepository.findAll().stream().filter(captcha -> captcha.getTime().compareTo(time) < 0)
                .forEach(captchaRepository::delete);
    }
}
