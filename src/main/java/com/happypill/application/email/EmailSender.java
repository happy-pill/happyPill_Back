package com.happypill.application.email;

public interface EmailSender {

    void send(String subject, String content);
}
