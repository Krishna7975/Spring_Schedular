package com.example.schedule.email;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class SendGridEmailService {

    @Value("${email.sendgrid.from-email}")
    private String fromEmail;


    public void sendTextEmail(String toEmails, String carbonCopy, String subject, String emailContent) throws IOException {
        log.info("to Emails- {}", toEmails);

        List<String> emailList = Arrays.asList(toEmails.split(","));
        emailList.forEach(toEmail -> {
            log.info("sending mail to {}", toEmail);
            Email from = new Email(fromEmail);
            Email to = new Email(toEmail.trim());
            Content content = new Content("text/html", emailContent);
            Mail mail = new Mail(from, subject, to, content);

            Personalization personalization = new Personalization();
            Arrays.asList(carbonCopy.split(",")).forEach(ccEmail -> {
                personalization.addCc(new Email(ccEmail.trim()));
            });
            personalization.addTo(to);
            mail.addPersonalization(personalization);

            Request request = new Request();
            try {
                request.setMethod(Method.POST.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());
                log.info(request.getBody());
                log.info("Mail Sent!");
            } catch (IOException ex) {
                log.error("ex while sending mail- ", ex);
            }
        });
    }
}
