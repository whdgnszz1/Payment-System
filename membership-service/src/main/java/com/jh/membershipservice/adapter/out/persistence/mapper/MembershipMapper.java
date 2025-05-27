package com.jh.membershipservice.adapter.out.persistence.mapper;

import com.jh.membershipservice.adapter.out.persistence.jpa.entity.MembershipEntity;
import com.jh.membershipservice.domain.Membership;
import org.springframework.stereotype.Component;

@Component
public class MembershipMapper {
    public Membership mapToDomainEntity(
        MembershipEntity membership) {
        System.out.println(membership.toString());
        return Membership.generateMember(
            new Membership.MembershipId(membership.getMembershipId() + ""),
            new Membership.MembershipName(membership.getName()),
            new Membership.MembershipEmail(membership.getEmail()),
            new Membership.MembershipAddress(membership.getAddress()),
            new Membership.MembershipIsValid(membership.isValid()),
            new Membership.MembershipAggregateIdentifier(membership.getAggregateIdentifier())
        );
    }
}
