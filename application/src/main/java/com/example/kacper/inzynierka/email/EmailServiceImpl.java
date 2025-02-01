package com.example.kacper.inzynierka.email;

import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    private final String sender = "przetestujmycie@gmail.com";

    public String sendSimpleMail(EmailDetails details) {

        try {

            MimeMessage message = javaMailSender.createMimeMessage();
            message.setSubject(details.getSubject());
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message,true);
            helper.setFrom(sender);
            helper.setTo(details.getRecipient());
            helper.setText(details.getMsgBody(), true);
            javaMailSender.send(message);
            return "Mail wyslany";
        }

        catch (Exception e) {
            return "Blad podczas wysylania maila";
        }
    }

    @Override
    public String sendMailWithAttachment(EmailDetails details) {
        return null;
    }
}
