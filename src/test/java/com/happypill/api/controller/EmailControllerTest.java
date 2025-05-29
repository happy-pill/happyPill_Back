package com.happypill.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.happypill.api.oauth2.OAuth2UserPrincipal;
import com.happypill.application.service.dto.EmailService;
import com.happypill.application.service.dto.request.EmailVerifyConfirmRequest;
import com.happypill.application.service.dto.request.EmailVerifyRequest;
import com.happypill.application.service.dto.response.ApiResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmailController.class)
@AutoConfigureMockMvc(addFilters = false)
class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmailService emailService;

    @Disabled
    @DisplayName("유효한 이메일 전송 시 요청은 성공한다.")
    @Test
    void requestEmailCode_1() throws Exception {
        //given
        EmailVerifyRequest request = EmailVerifyRequest.of("test@gmail.com");

        OAuth2UserPrincipal principal = mock(OAuth2UserPrincipal.class);
        given(principal.getEmail()).willReturn("cdh3946@gmail.com");

        //when //then
        mockMvc.perform(post("/api/user/email/verification-request")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.authentication(
                                new UsernamePasswordAuthenticationToken(principal, null, Collections.emptyList())
                        )))
                .andExpect(status().isOk());

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
    @Disabled
    @DisplayName("인증되지 않은 사용자의 이메일 전송 요청은 실패한다.")
    @Test
    void requestEmailCode_3() throws Exception {
        //given
        EmailVerifyRequest request = EmailVerifyRequest.of("test@gmail.com");

        //when //then
        MvcResult result = mockMvc.perform(post("/api/user/email/verification-request")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(anonymous())) // 인증되지 않은 사용자
                .andExpect(status().isUnauthorized())
                .andReturn();

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