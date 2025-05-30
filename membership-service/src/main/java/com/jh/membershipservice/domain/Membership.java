package com.jh.membershipservice.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "회원 정보")
public class Membership {
    /**
     * The baseline balance of the account. This was the balance of the account before the first
     * activity in the activityWindow.
     */
    @Getter 
    @Schema(description = "회원 ID", example = "1")
    private final String membershipId;
    
    @Getter 
    @Schema(description = "회원 이름", example = "홍길동")
    private final String name;
    
    @Getter 
    @Schema(description = "회원 이메일", example = "hong@example.com")
    private final String email;
    
    @Getter 
    @Schema(description = "회원 주소", example = "서울시 강남구")
    private final String address;
    
    @Getter 
    @Schema(description = "회원 유효성", example = "true")
    private final boolean isValid;
    
    @Getter 
    @Schema(description = "집계 식별자")
    private final String aggregateIdentifier;

    public static Membership generateMember(
            MembershipId membershipId, MembershipName membershipName, MembershipEmail membershipEmail, MembershipAddress membershipAddress, MembershipIsValid membershipIsValid,
            MembershipAggregateIdentifier membershipAggregateIdentifier) {
        return new Membership(
                membershipId.membershipId,
                membershipName.nameValue,
                membershipEmail.emailValue,
                membershipAddress.addressValue,
                membershipIsValid.isValidValue,
                membershipAggregateIdentifier.aggregateIdentifier
        );
    }

    @Value
    public static class MembershipId {
        public MembershipId(String value) {
            this.membershipId = value;
        }
        String membershipId ;
    }

    @Value
    public static class MembershipName {
        public MembershipName(String value) {
            this.nameValue = value;
        }

        String nameValue;
    }
    @Value
    public static class MembershipEmail {
        public MembershipEmail(String value) {
            this.emailValue = value;
        }
        String emailValue;
    }

    @Value
    public static class MembershipAddress {
        public MembershipAddress(String value) {
            this.addressValue = value;
        }
        String addressValue;
    }

    @Value
    public static class MembershipIsValid {
        public MembershipIsValid(boolean value) {
            this.isValidValue = value;
        }
        boolean isValidValue;
    }

    @Value
    public static class MembershipAggregateIdentifier {
        public MembershipAggregateIdentifier(String value) {
            this.aggregateIdentifier = value;
        }
        String aggregateIdentifier;
    }
}