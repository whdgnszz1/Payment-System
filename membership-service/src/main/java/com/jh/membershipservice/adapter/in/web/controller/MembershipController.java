package com.jh.membershipservice.adapter.in.web.controller;

import com.jh.common.WebAdapter;
import com.jh.membershipservice.adapter.in.web.dto.RegisterMembershipReq;
import com.jh.membershipservice.application.port.in.MembershipUseCase;
import com.jh.membershipservice.application.port.in.RegisterMembershipCommand;
import com.jh.membershipservice.domain.Membership;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipUseCase membershipUseCase;

    @PostMapping("/membership/register")
    Membership registerMembership(@RequestBody RegisterMembershipReq request) {

        RegisterMembershipCommand command = RegisterMembershipCommand.builder()
            .name(request.getName())
            .address(request.getAddress())
            .email(request.getEmail())
            .isValid(true)
            .build();


        return membershipUseCase.registerMembership(command);
    }
}
