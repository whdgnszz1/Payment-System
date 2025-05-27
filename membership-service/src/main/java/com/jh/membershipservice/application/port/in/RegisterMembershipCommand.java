package com.jh.membershipservice.application.port.in;

import com.jh.common.SelfValidating;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class RegisterMembershipCommand extends SelfValidating<RegisterMembershipCommand> {

    @NotNull
    @TargetAggregateIdentifier
    private String membershipId;

    private String name;

    private String address;

    private String email;

    private boolean isValid;
}
