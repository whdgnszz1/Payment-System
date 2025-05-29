package com.jh.membershipservice.application.port.out;

import com.jh.membershipservice.domain.Membership;

public interface MembershipPort {

    Membership createMembership(
        Membership.MembershipName membershipName
        , Membership.MembershipEmail membershipEmail
        , Membership.MembershipAddress membershipAddress
        , Membership.MembershipIsValid membershipIsValid
        , Membership.MembershipAggregateIdentifier membershipAggregateIdentifier
    );

    Membership findMembership(
            Membership.MembershipId membershipId
    );
}
