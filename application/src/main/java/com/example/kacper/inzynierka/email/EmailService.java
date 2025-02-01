package com.example.kacper.inzynierka.email;


import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

// Interface

public interface EmailService {

    // Method
    // To send a simple email
    String sendSimpleMail(EmailDetails details);

    // Method
    // To send an email with attachment
    String sendMailWithAttachment(EmailDetails details);
}
