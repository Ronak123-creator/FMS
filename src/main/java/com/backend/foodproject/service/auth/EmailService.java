package com.backend.foodproject.service.auth;

import com.backend.foodproject.entity.UserInfo;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(UserInfo userInfo){
        String subject = "Verify Your Email";
        String verficationUrl = "http://localhost:8080/api/auth/verify?token=" +
                userInfo.getVerificationToken();
        String message = "Welcome " + userInfo.getName() + "\n\n"
                + "Please click the link to verify your email: \n"
                + verficationUrl +"\n\n Thank You!!";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userInfo.getEmail());
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);

    }
}

