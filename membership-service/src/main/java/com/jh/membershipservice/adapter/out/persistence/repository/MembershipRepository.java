package com.jh.membershipservice.adapter.out.persistence.repository;

import com.jh.membershipservice.adapter.out.persistence.jpa.entity.MembershipEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<MembershipEntity, Long> {
}
