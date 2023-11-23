package com.kusithm.meetupd.domain.user.mysql;

import com.kusithm.meetupd.domain.user.entity.UserTicketSpend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTicketSpendRepository extends JpaRepository<UserTicketSpend, Long> {

    Boolean existsByUserIdAndPurchaseUserId(Long userId, Long purchaseUserId);

}
