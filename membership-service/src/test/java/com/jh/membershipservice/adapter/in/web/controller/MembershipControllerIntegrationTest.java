package com.jh.membershipservice.adapter.in.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jh.membershipservice.adapter.in.web.dto.RegisterMembershipReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("MembershipController 통합 테스트")
class MembershipControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원 등록 및 조회 - 전체 플로우 테스트")
    void membershipFullFlow_Success() throws Exception {
        // given - 회원 등록 요청 데이터
        RegisterMembershipReq registerRequest = new RegisterMembershipReq();
        registerRequest.setName("김철수");
        registerRequest.setEmail("kim@example.com");
        registerRequest.setAddress("부산시 해운대구");

        // when & then - 회원 등록
        String response = mockMvc.perform(post("/membership/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value("김철수"))
            .andExpect(jsonPath("$.email").value("kim@example.com"))
            .andExpect(jsonPath("$.address").value("부산시 해운대구"))
            .andExpect(jsonPath("$.valid").value(true))
            .andExpect(jsonPath("$.membershipId").exists())
            .andReturn()
            .getResponse()
            .getContentAsString();

        // 등록된 회원의 ID 추출
        String membershipId = objectMapper.readTree(response).get("membershipId").asText();

        // when & then - 등록된 회원 조회
        mockMvc.perform(get("/membership/{membershipId}", membershipId))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.membershipId").value(membershipId))
            .andExpect(jsonPath("$.name").value("김철수"))
            .andExpect(jsonPath("$.email").value("kim@example.com"))
            .andExpect(jsonPath("$.address").value("부산시 해운대구"))
            .andExpect(jsonPath("$.valid").value(true));
    }

    @Test
    @DisplayName("여러 회원 등록 테스트")
    void multipleMembers_Registration() throws Exception {
        // given - 첫 번째 회원
        RegisterMembershipReq firstRequest = new RegisterMembershipReq();
        firstRequest.setName("홍길동");
        firstRequest.setEmail("hong@example.com");
        firstRequest.setAddress("서울시 강남구");

        // given - 두 번째 회원
        RegisterMembershipReq secondRequest = new RegisterMembershipReq();
        secondRequest.setName("김영희");
        secondRequest.setEmail("kim@example.com");
        secondRequest.setAddress("부산시 해운대구");

        // when & then - 첫 번째 회원 등록
        mockMvc.perform(post("/membership/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firstRequest)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("홍길동"));

        // when & then - 두 번째 회원 등록
        mockMvc.perform(post("/membership/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(secondRequest)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("김영희"));
    }
} 