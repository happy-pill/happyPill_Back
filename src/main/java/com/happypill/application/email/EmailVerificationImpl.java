package com.happypill.application.email;

public class EmailVerificationImpl implements EmailVerification {

    @Override
    public String generateCode(String email) {
        return null;
    }

    @Override
    public String buildEmailContent(String code) {
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
    public boolean confirmCode(String email, String code) {
        return true;
    }
}
