package com.example.schedule.service;

import com.example.schedule.email.SendGridEmailService;
import com.example.schedule.email.ThymeleafUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class Scheduler {

    private SendGridEmailService sendGridEmailService;

    @Autowired
    public Scheduler(SendGridEmailService sendGridEmailService) {
        this.sendGridEmailService = sendGridEmailService;
    }

    @Value("${email.sendgrid.from-email}")
    private String fromEmail;
    @Value("${email.sendgrid.to-email}")
    private String toEmail;

    @Value("${email.sendgrid.cc-email}")
    private String ccMail;


//    @Scheduled(cron = "${scheduler.cron}")
    @Scheduled(cron = "*/10 * * * * *")
    public void schedulerTime() throws IOException {

        String body = "Body for this mail ";
        try {
            log.info("email body");
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("body", body);
            String emailContent = ThymeleafUtils.parseThymeleafTemplate("body_for_email", attributes);
            sendGridEmailService.sendTextEmail(toEmail, ccMail, "Subject for testing mail", emailContent);
        } catch (Exception e) {
            log.info("error while sending mail", e);
        }
    }
}