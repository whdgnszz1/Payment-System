package com.jh.membershipservice.adapter.in.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jh.membershipservice.adapter.in.web.dto.RegisterMembershipReq;
import com.jh.membershipservice.adapter.in.web.exception.GlobalExceptionHandler;
import com.jh.membershipservice.application.port.in.FindMembershipCommand;
import com.jh.membershipservice.application.port.in.MembershipUseCase;
import com.jh.membershipservice.application.port.in.RegisterMembershipCommand;
import com.jh.membershipservice.domain.Membership;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MembershipController 테스트")
class MembershipControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private MembershipUseCase membershipUseCase;

    @InjectMocks
    private MembershipController membershipController;

    private RegisterMembershipReq registerRequest;
    private Membership mockMembership;

    @BeforeEach
    void setUp() {
        // MockMvc 설정 (전역 예외 처리기 포함)
        mockMvc = MockMvcBuilders.standaloneSetup(membershipController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
        objectMapper = new ObjectMapper();

        // 테스트용 요청 데이터 설정
        registerRequest = new RegisterMembershipReq();
        registerRequest.setName("홍길동");
        registerRequest.setEmail("hong@example.com");
        registerRequest.setAddress("서울시 강남구");

        // 테스트용 응답 데이터 설정
        mockMembership = Membership.generateMember(
            new Membership.MembershipId("1"),
            new Membership.MembershipName("홍길동"),
            new Membership.MembershipEmail("hong@example.com"),
            new Membership.MembershipAddress("서울시 강남구"),
            new Membership.MembershipIsValid(true),
            new Membership.MembershipAggregateIdentifier("agg-1")
        );
    }

    @Test
    @DisplayName("회원 등록 - 성공")
    void registerMembership_Success() throws Exception {
        // given
        given(membershipUseCase.registerMembership(any(RegisterMembershipCommand.class)))
            .willReturn(mockMembership);

        // when & then
        mockMvc.perform(post("/membership/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.membershipId").value("1"))
            .andExpect(jsonPath("$.name").value("홍길동"))
            .andExpect(jsonPath("$.email").value("hong@example.com"))
            .andExpect(jsonPath("$.address").value("서울시 강남구"))
            .andExpect(jsonPath("$.valid").value(true));

        // verify
        verify(membershipUseCase).registerMembership(any(RegisterMembershipCommand.class));
    }

    @Test
    @DisplayName("회원 등록 - 잘못된 요청 (빈 이름)")
    void registerMembership_InvalidRequest_EmptyName() throws Exception {
        // given
        registerRequest.setName("");
        given(membershipUseCase.registerMembership(any(RegisterMembershipCommand.class)))
            .willReturn(mockMembership);

        // when & then
        mockMvc.perform(post("/membership/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
            .andDo(print())
            .andExpect(status().isOk()); // 현재는 validation이 없으므로 200 반환
    }

    @Test
    @DisplayName("회원 등록 - 잘못된 요청 (null 요청 본문)")
    void registerMembership_InvalidRequest_NullBody() throws Exception {
        // given
        given(membershipUseCase.registerMembership(any(RegisterMembershipCommand.class)))
            .willReturn(mockMembership);

        // when & then
        mockMvc.perform(post("/membership/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andDo(print())
            .andExpect(status().isOk()); // 현재는 validation이 없으므로 200 반환
    }

    @Test
    @DisplayName("회원 조회 - 성공")
    void findMembershipByMemberId_Success() throws Exception {
        // given
        String membershipId = "1";
        given(membershipUseCase.findMembership(any(FindMembershipCommand.class)))
            .willReturn(mockMembership);

        // when & then
        mockMvc.perform(get("/membership/{membershipId}", membershipId))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.membershipId").value("1"))
            .andExpect(jsonPath("$.name").value("홍길동"))
            .andExpect(jsonPath("$.email").value("hong@example.com"))
            .andExpect(jsonPath("$.address").value("서울시 강남구"))
            .andExpect(jsonPath("$.valid").value(true));

        // verify
        verify(membershipUseCase).findMembership(any(FindMembershipCommand.class));
    }

    @Test
    @DisplayName("회원 조회 - 존재하지 않는 회원")
    void findMembershipByMemberId_NotFound() throws Exception {
        // given
        String membershipId = "999";
        given(membershipUseCase.findMembership(any(FindMembershipCommand.class)))
            .willThrow(new RuntimeException("회원을 찾을 수 없습니다"));

        // when & then
        mockMvc.perform(get("/membership/{membershipId}", membershipId))
            .andDo(print())
            .andExpect(status().isNotFound()) // 404 상태 코드 기대
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value("MEMBER_NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("회원을 찾을 수 없습니다"))
            .andExpect(jsonPath("$.status").value(404));

        // verify
        verify(membershipUseCase).findMembership(any(FindMembershipCommand.class));
    }

    @Test
    @DisplayName("회원 조회 - 잘못된 경로 파라미터")
    void findMembershipByMemberId_InvalidPathParameter() throws Exception {
        // when & then
        mockMvc.perform(get("/membership/"))
            .andDo(print())
            .andExpect(status().isNotFound()) // 경로가 매칭되지 않아 404
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value("NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("요청한 리소스를 찾을 수 없습니다"))
            .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("회원 등록 - 서버 내부 오류")
    void registerMembership_InternalServerError() throws Exception {
        // given
        given(membershipUseCase.registerMembership(any(RegisterMembershipCommand.class)))
            .willThrow(new RuntimeException("데이터베이스 연결 오류"));

        // when & then
        mockMvc.perform(post("/membership/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
            .andDo(print())
            .andExpect(status().isInternalServerError())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value("INTERNAL_SERVER_ERROR"))
            .andExpect(jsonPath("$.message").value("서버 내부 오류가 발생했습니다"))
            .andExpect(jsonPath("$.status").value(500));

        // verify
        verify(membershipUseCase).registerMembership(any(RegisterMembershipCommand.class));
    }

    @Test
    @DisplayName("회원 등록 - 잘못된 인자")
    void registerMembership_IllegalArgument() throws Exception {
        // given
        given(membershipUseCase.registerMembership(any(RegisterMembershipCommand.class)))
            .willThrow(new IllegalArgumentException("이메일 형식이 올바르지 않습니다"));

        // when & then
        mockMvc.perform(post("/membership/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("이메일 형식이 올바르지 않습니다"))
            .andExpect(jsonPath("$.status").value(400));

        // verify
        verify(membershipUseCase).registerMembership(any(RegisterMembershipCommand.class));
    }
} 