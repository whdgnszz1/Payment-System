package com.jh.membershipservice.adapter.out.persistence.adapter;

import com.jh.common.PersistenceAdapter;
import com.jh.membershipservice.adapter.out.persistence.jpa.entity.MembershipEntity;
import com.jh.membershipservice.adapter.out.persistence.mapper.MembershipMapper;
import com.jh.membershipservice.adapter.out.persistence.repository.MembershipRepository;
import com.jh.membershipservice.application.port.out.MembershipPort;
import com.jh.membershipservice.domain.Membership;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class MembershipPersistenceAdapter implements MembershipPort {

    private final MembershipRepository membershipRepository;
    private final MembershipMapper membershipMapper;

    @Override
    public Membership createMembership(Membership.MembershipName membershipName
        , Membership.MembershipEmail membershipEmail
        , Membership.MembershipAddress membershipAddress
        , Membership.MembershipIsValid membershipIsValid
        , Membership.MembershipAggregateIdentifier membershipAggregateIdentifier
    ) {
        MembershipEntity savedEntity = membershipRepository.save(
            new MembershipEntity(
                membershipName.getNameValue(),
                membershipEmail.getEmailValue(),
                membershipAddress.getAddressValue(),
                membershipIsValid.isValidValue(),
                membershipAggregateIdentifier.getAggregateIdentifier()
            )
        );
        
        return membershipMapper.mapToDomainEntity(savedEntity);
    }

    @Override
    public Membership findMembership(Membership.MembershipId membershipId) {
        return membershipMapper.mapToDomainEntity(
                membershipRepository.getById(Long.parseLong(membershipId.getMembershipId()))
        );
    }

}
