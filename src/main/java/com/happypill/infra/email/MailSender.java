package com.happypill.infra.email;

public interface MailSender {
    void send(String loginEmail, String newEmail);
}
