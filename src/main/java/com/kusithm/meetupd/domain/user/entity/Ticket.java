package com.kusithm.meetupd.domain.user.entity;


import com.kusithm.meetupd.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity(name = "USER_TICKET")
public class Ticket extends BaseEntity {

    @Id
    @Column(name = "ticket_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "count")
    private Integer count;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Ticket createInitTicket() {
        return Ticket.builder()
                .count(0)
                .build();
    }

    public void changeUser(User user) {
        this.user = user;
        user.updateTicket(this);
    }

    public void addTicketCount(Integer amount) {
        this.count += amount;
    }

    public void spendTicket() {
        this.count -= 1;
    }
}
