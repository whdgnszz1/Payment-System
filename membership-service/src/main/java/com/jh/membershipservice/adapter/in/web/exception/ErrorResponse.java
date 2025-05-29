package com.jh.membershipservice.adapter.in.web.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "에러 응답")
public class ErrorResponse {
    
    @Schema(description = "에러 코드", example = "MEMBER_NOT_FOUND")
    private String code;
    
    @Schema(description = "에러 메시지", example = "회원을 찾을 수 없습니다")
    private String message;
    
    @Schema(description = "HTTP 상태 코드", example = "404")
    private int status;
} 