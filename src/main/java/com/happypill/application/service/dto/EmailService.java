package com.happypill.application.service.dto;

import com.happypill.application.exception.EmailException;
import com.happypill.application.repository.happypilluser.HappypillUserRepository;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService { //이메일 인증코드 전송을 처리하는 로직

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final long CODE_EXPIRATION_TIME = 180;

    private final HappypillUserRepository userRepository;
    private final EmailRequestLimitService emailRequestLimitService;
    private final RedisTemplate<String, String> redisTemplateString;
    private final JavaMailSender javaMailSender;

    private String getKey(String loginEmail) {
        return "NewEmailRequest:" + loginEmail;
    }

    public String generateCode() {
        return secureRandom.ints(6, 0, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }

    public void sendEmail(String loginEmail, String newEmail) {
        try {
            // newEmail 이 본인의 loginEmail 도 아니고 누군가의 loginEmail 또는 notifyEmail 로 이미 사용 중이면 예외처리
            if (!newEmail.equals(loginEmail) &&
                    (userRepository.existsByNotifyEmail(newEmail) || userRepository.existsByLoginEmail(newEmail))) {
                throw new EmailException("이미 사용 중인 이메일입니다.", HttpStatus.BAD_REQUEST);
            }
            // 사용자의 인증요청 가능횟수을 기준으로 요청할 수 있는지 확인
            if (!emailRequestLimitService.canRequestCode(loginEmail)) {
                throw new EmailException("인증요청 횟수를 초과하였습니다.", HttpStatus.BAD_REQUEST);
            }
            String code = generateCode();

            MimeMessage message = javaMailSender.createMimeMessage();
            message.setRecipients(Message.RecipientType.TO, newEmail);
            message.setSubject("해피필 인증코드");
            message.setText(emailContent(code), "utf-8", "html");
            message.setFrom(new InternetAddress("cdh3946@gmail.com", "HappyPill"));
            javaMailSender.send(message);

            String key = getKey(newEmail);

            //newEmail 을 기준으로 3분 유효시간 부여
            redisTemplateString.opsForValue().set(key, code, CODE_EXPIRATION_TIME, TimeUnit.SECONDS);

            //loginEmail 을 기준으로 [요청횟수] -1 차감
            emailRequestLimitService.decreaseRequestCnt(loginEmail);
        } catch(EmailException e){
            throw e;
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("이메일 전송 중 문제 발생 : {}", e.getMessage());
            throw new EmailException("이메일 전송 실패", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("인증 이메일 요청 처리 중 예외 발생 : {}", e.getMessage());
            throw new EmailException("서버 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String emailContent(String code) {
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

    public void confirmCode(String newEmail, String code) {
        try{
            String key = getKey(newEmail);
            String storedCode = redisTemplateString.opsForValue().get(key);

            if (storedCode == null || !code.equals(storedCode)) {
                throw new EmailException("인증코드가 일치하지 않거나 만료되었습니다.", HttpStatus.BAD_REQUEST);
            }
            redisTemplateString.delete(key);
        }catch(EmailException e){
            throw e;
        } catch(Exception e){
            throw new EmailException("서버 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}