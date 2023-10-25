package com.wanted.preonboarding.mail;

import com.wanted.preonboarding.util.RedisUtil;
import java.util.NoSuchElementException;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;

    public String getRandomNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(900000) + 100000;
        return String.valueOf(randomNumber);
    }

    public boolean validateSetAuthCode(String email) {
        String redisValue = redisUtil.getData("authCode:" + email);

        if (redisValue == null) {
            throw new NoSuchElementException("authentication code isn't saved for mail : " + email);
        }
        return true;
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public boolean authenticateRandomNumber(String randomNumber, String email) {
        String redisValue = redisUtil.getData("authCode:" + email);

        if (redisValue == null) {
            throw  new NoSuchElementException("authentication code expired for mail : " + email);
        }
        if (redisValue.equals(randomNumber)) {
            return false;
        }
        return true;
    }
}
