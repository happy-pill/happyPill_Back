package com.happypill.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.happypill.application.service.dto.EmailService;
import com.happypill.application.service.dto.request.EmailVerifyConfirmRequest;
import com.happypill.application.service.dto.request.EmailVerifyRequest;
import com.happypill.application.service.dto.response.ApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false) //security 무시
@WebMvcTest(controllers = EmailController.class)
class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmailService emailService;

    @DisplayName("유효한 이메일 전송 시 요청은 성공한다.")
    @Test
    void requestEmailCode_1() throws Exception {
        //given
        EmailVerifyRequest request = EmailVerifyRequest.of("test@gmail.com");

        //when //then
        MvcResult result = mockMvc.perform(post("/api/user/email/verification-request")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString(); // 응답 바디를 문자열로

        ApiResponse apiResponse = objectMapper.readValue(content, ApiResponse.class);

        assertThat(apiResponse.message()).isEqualTo("인증코드가 전송되었습니다.");
        verify(emailService).sendEmail("cdh3946@gmail.com", request.newEmail());
    }

    @DisplayName("유효하지 않은 이메일 전송 시 요청은 실패한다.")
    @Test
    void requestEmailCode_2() throws Exception {
        //given
        EmailVerifyRequest request = EmailVerifyRequest.of("test@.com");

        //when //then
        MvcResult result = mockMvc.perform(post("/api/user/email/verification-request")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        ApiResponse apiResponse = objectMapper.readValue(content, ApiResponse.class);

        assertThat(apiResponse.message()).isEqualTo("올바른 이메일 형식이어야 합니다.");
    }

    @DisplayName("유효한 코드 전송 시 요청은 성공한다.")
    @Test
    void confirmEmailCode_1() throws Exception {
        //given
        EmailVerifyConfirmRequest request = EmailVerifyConfirmRequest.of("test@gmail.com", "123456");

        //when //then
        MvcResult result = mockMvc.perform(post("/api/user/email/verification-confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        ApiResponse apiResponse = objectMapper.readValue(content, ApiResponse.class);

        assertThat(apiResponse.message()).isEqualTo("인증 완료");
    }

    @DisplayName("유효하지 않은 코드 전송 시 요청은 실패한다.")
    @Test
    void confirmEmailCode_2() throws Exception {
        //given
        EmailVerifyConfirmRequest request = EmailVerifyConfirmRequest.of("test@gmail.com", "12345");

        //when //then
        MvcResult result = mockMvc.perform(post("/api/user/email/verification-confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        ApiResponse apiResponse = objectMapper.readValue(content, ApiResponse.class);

        assertThat(apiResponse.message()).isEqualTo("인증코드는 6자리 숫자여야 합니다.");
    }
}