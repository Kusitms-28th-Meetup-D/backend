package com.kusithm.meetupd.domain.user.entity;

import com.kusithm.meetupd.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity(name = "USER_TICKET_SPEND")
public class UserTicketSpend extends BaseEntity {

    @Id
    @Column(name = "user_ticket_spend_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "purchase_user_id")
    private Long purchaseUserId;

    public static UserTicketSpend createUserTicketSpend(Long userId, Long purchaseUserId) {
        return UserTicketSpend.builder()
                .userId(userId)
                .purchaseUserId(purchaseUserId)
                .build();
    }

}
