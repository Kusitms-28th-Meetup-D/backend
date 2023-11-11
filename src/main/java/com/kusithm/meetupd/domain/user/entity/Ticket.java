package com.kusithm.meetupd.domain.user.entity;


import com.kusithm.meetupd.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Ticket extends BaseEntity {

    @Id
    @Column(name = "ticket_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "count")
    private Integer count;

    public static Ticket createInitTicket() {
        return Ticket.builder()
                .count(0)
                .build();
    }

    public void addTicketCount(Integer amount) {
        this.count += amount;
    }

    public void spendTicket() {
        this.count -= 1;
    }
}
