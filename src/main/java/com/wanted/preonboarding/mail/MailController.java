package com.wanted.preonboarding.mail;

import com.wanted.preonboarding.util.RedisUtil;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;
    private final RedisUtil redisUtil;

    @PostMapping
    public void getRandomNumberApi(@RequestBody MailDto mailDto) {
        String email = mailDto.getEmail();
        String randomNumber;

        if (mailService.checkIsAuthCode(email) == null) {
            randomNumber = mailService.getRandomNumber();
            redisUtil.setDataExpireWithPrefix("authCode", email, randomNumber, Duration.ofMinutes(30));
            mailService.validateSetAuthCode(email);
        } else {
            randomNumber = mailService.checkIsAuthCode(email);
        }

        mailService.sendSimpleMessage(email, "이메일 인증 번호 발송", "6 자리 숫자: " + randomNumber);
    }
}
