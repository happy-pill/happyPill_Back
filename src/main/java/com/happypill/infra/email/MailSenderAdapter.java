package com.happypill.infra.email;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MailSenderAdapter implements MailSender{

    private static final long CODE_EXPIRATION_TIME = 180;
    private static final SecureRandom secureRandom = new SecureRandom();

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public String generateCode() {
        return secureRandom.ints(6, 0, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }

    @Override
    public void send(String loginEmail, String newEmail) {
        try {
            // 다른 유저와의 이메일 중복 체크

            String code = generateCode();

            MimeMessage message = javaMailSender.createMimeMessage();
            message.setRecipients(Message.RecipientType.TO, loginEmail);
            message.setSubject(newEmail);
            message.setText(emailContent(code), "utf-8", "html");
            message.setFrom(new InternetAddress(senderEmail, "HappyPill"));
            javaMailSender.send(message);

            // 3분 유효시간 부여 로직
            // 요청 횟수 -1 차감 로직

        } catch(MessagingException | UnsupportedEncodingException e){

        } catch (Exception e) {

        }
    }

    @Override
    public String emailContent(String code) {
        return """
                  <div style="font-family: 'Arial', sans-serif; line-height: 1.6;">
                     <p>[해피필] 회원님의 요청에 따라 아래의 인증코드를 발급해드립니다.</p>
                     <br>
                  <p style="font-size: 24px; font-weight: bold; color: #2d3748;">
                    인증코드: <span style="background-color: #f1f5f9; padding: 4px 8px; border-radius: 4px;">%s</span>
                  </p>
                  
                  <br>
                  <p>해당 인증코드는 <strong>3분간 유효</strong>하며, 타인에게 절대 공유하지 마세요.</p>
                  
                  <p>
                    만약 본인이 요청한 것이 아니라면, 즉시 고객센터로 문의해 주세요.<br>
                    감사합니다.
                  </p>
                    
                  <hr style="border: none; border-top: 1px solid #e2e8f0;">
                  <p style="font-size: 12px; color: #718096;">
                    이 메일은 발신전용입니다. 문의는 홈페이지를 이용해 주세요.
                  </p>
                </div>
                """.formatted(code);
    }

    @Override
    public void confirmCode(String newEmail, String code) {
        try{
            // 인증코드 검증 로직
        }catch (Exception e){

        }
    }
}