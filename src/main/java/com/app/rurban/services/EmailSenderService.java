package com.app.rurban.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    JavaMailSender javaMailSender;

    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("rurbanorg@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        javaMailSender.send(message);
        System.out.println("Mail sent successfully...");
    }

    public void sendMimeEmail(String name, String toEmail, String subject, String token) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        try {
            helper.setFrom("rurbanorg@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            String emailLink = "https://rurban-be.onrender.com/api/v1/auth/verifyEmail?email=" + toEmail + "&token=" + token;
            String htmlContent = "<div style='margin: auto; text-align: center;'>" +
                    "<img alt='rurban' src='https://rurban-fe.onrender.com/7.png' />" +
                    "<p></p>" +
                    "<span style='font-size: 17px; font-weight: 300;'>Hi " + name + "</span>" +
                    "<p></p>" +
                    "<span style='font-size: 30px; font-weight: 300;'>Thank you for registering with Rurban!</span>" +
                    "<p></p>" +
                    "<span style='font-size: 14px; font-weight: 300;'>To complete your registration and activate your account, we just need to verify your email address.</span>" +
                    "<p></p>" +
                    "<span style='font-size: 14px; font-weight: 300;'>Please click the button below to confirm your email:</span>" +
                    "<p></p>" +
                    "<br />" +
                    "<a href=" + emailLink + " style='background: green; border: 1px solid green; border-radius: 4px; padding: 10px; text-decoration: none; color: #fff;'>CONFIRM EMAIL ADDRESS</a>" +
                    "<p></p>" +
                    "<br />" +
                    "<span style='font-size: 14px; font-weight: 300;'>Thanks,</span>" +
                    "<p></p>" +
                    "<span style='font-size: 25px; font-weight: 500;'>The Rurban Team</span>" +
                    "</div>";
            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
            System.out.println("Mail sent successfully...");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
