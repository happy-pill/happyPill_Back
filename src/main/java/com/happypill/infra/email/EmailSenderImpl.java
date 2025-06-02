package com.happypill.infra.email;

import com.happypill.application.email.EmailSender;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
public class EmailSenderImpl implements EmailSender {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public void send(String subject, String content) {
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setRecipients(Message.RecipientType.TO, subject);
            message.setSubject("해피필 인증코드");
            message.setText(content, "utf-8", "html");
            message.setFrom(new InternetAddress(senderEmail, "HappyPill"));
            javaMailSender.send(message);
        }catch (MessagingException | UnsupportedEncodingException e){

        }
    }
}