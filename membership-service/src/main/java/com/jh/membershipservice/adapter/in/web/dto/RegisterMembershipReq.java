package com.jh.membershipservice.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "회원 등록 요청")
public class RegisterMembershipReq {
    
    @Schema(description = "회원 이름", example = "홍길동", required = true)
    private String name;

    @Schema(description = "회원 주소", example = "서울시 강남구", required = true)
    private String address;

    @Schema(description = "회원 이메일", example = "hong@example.com", required = true)
    private String email;
}
