package com.happypill.infra.email;


import com.happypill.application.client.EmailSender;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoOpsEmailSender implements EmailSender {

    @Override
    public void send(String subject, String content) {
        log.info("subject = {} content={}", subject, content);
    }
}
