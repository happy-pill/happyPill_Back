package com.happypill.application.client;

public interface EmailSender {

    void send(String subject, String content);
}
