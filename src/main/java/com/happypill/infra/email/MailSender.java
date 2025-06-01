package com.happypill.infra.email;

public interface MailSender {

    String generateCode();

    void send(String loginEmail, String newEmail);

    String emailContent(String code);

    void confirmCode(String newEmail, String code);
}
