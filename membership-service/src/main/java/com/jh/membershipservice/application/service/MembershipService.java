package com.jh.membershipservice.application.service;

import com.jh.common.UseCase;
import com.jh.membershipservice.application.port.in.MembershipUseCase;
import com.jh.membershipservice.application.port.in.RegisterMembershipCommand;
import com.jh.membershipservice.application.port.out.RegisterMembershipPort;
import com.jh.membershipservice.domain.Membership;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@UseCase
@Transactional
public class MembershipService implements MembershipUseCase {

    private final RegisterMembershipPort registerMembershipPort;

    @Override
    public Membership registerMembership(RegisterMembershipCommand command) {
        return registerMembershipPort.createMembership(
            new Membership.MembershipName(command.getName()),
            new Membership.MembershipEmail(command.getEmail()),
            new Membership.MembershipAddress(command.getAddress()),
            new Membership.MembershipIsValid(command.isValid()),
            new Membership.MembershipAggregateIdentifier("default")
        );
    }
}
