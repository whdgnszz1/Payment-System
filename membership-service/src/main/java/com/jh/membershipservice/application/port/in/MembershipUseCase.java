package com.jh.membershipservice.application.port.in;

import com.jh.common.UseCase;
import com.jh.membershipservice.domain.Membership;

@UseCase
public interface MembershipUseCase {

    Membership registerMembership(RegisterMembershipCommand registerMembershipCommand);
}
