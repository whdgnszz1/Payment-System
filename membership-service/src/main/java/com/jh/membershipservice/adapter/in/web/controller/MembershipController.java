package com.jh.membershipservice.adapter.in.web.controller;

import com.jh.common.WebAdapter;
import com.jh.membershipservice.adapter.in.web.dto.RegisterMembershipReq;
import com.jh.membershipservice.application.port.in.MembershipUseCase;
import com.jh.membershipservice.application.port.in.RegisterMembershipCommand;
import com.jh.membershipservice.domain.Membership;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@WebAdapter
@RestController
@RequiredArgsConstructor
@Tag(name = "Membership", description = "회원 관리 API")
public class MembershipController {

    private final MembershipUseCase membershipUseCase;

    @Operation(
        summary = "회원 등록",
        description = "새로운 회원을 등록합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "회원 등록 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/membership/register")
    Membership registerMembership(
        @Parameter(description = "회원 등록 요청 정보", required = true)
        @RequestBody RegisterMembershipReq request) {

        RegisterMembershipCommand command = RegisterMembershipCommand.builder()
            .name(request.getName())
            .address(request.getAddress())
            .email(request.getEmail())
            .isValid(true)
            .build();

        return membershipUseCase.registerMembership(command);
    }
}
