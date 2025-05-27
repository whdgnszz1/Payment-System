package com.jh.membershipservice.application.port.out;

import com.jh.membershipservice.domain.Membership;

public interface RegisterMembershipPort {

    Membership createMembership(
        Membership.MembershipName membershipName
        , Membership.MembershipEmail membershipEmail
        , Membership.MembershipAddress membershipAddress
        , Membership.MembershipIsValid membershipIsValid
        , Membership.MembershipAggregateIdentifier membershipAggregateIdentifier
    );
}
