package com.kusithm.meetupd.domain.user.service;

import com.kusithm.meetupd.common.error.EntityNotFoundException;
import com.kusithm.meetupd.common.error.ErrorCode;
import com.kusithm.meetupd.domain.user.dto.UserMypageResponseDto;
import com.kusithm.meetupd.domain.user.dto.request.BuyUserTicketRequestDto;
import com.kusithm.meetupd.domain.user.dto.response.UserTicketCountResponseDto;
import com.kusithm.meetupd.domain.user.dto.response.UserCheckResponseDto;
import com.kusithm.meetupd.domain.user.entity.*;
import com.kusithm.meetupd.domain.user.mysql.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserCheckResponseDto getUserByToken(Long userId) {
        System.out.println(userId);
        User findUser = getUserByUserId(userId);

        return UserCheckResponseDto.builder()
                .userId(findUser.getId())
                .username(findUser.getUsername())
                .build();
    }

    public UserMypageResponseDto getMypageUser(Long userId) {
        User findUser = getUserByUserId(userId);
        return new UserMypageResponseDto(findUser);
    }

    public UserTicketCountResponseDto buyUserTicket(Long userId, BuyUserTicketRequestDto request) {
        User findUser = getUserByUserId(userId);
        addUserTicket(findUser, request.getBuyAmount());
        return UserTicketCountResponseDto.of(findUser.getTicketCount());
    }

    private User getUserByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
        return user;
    }

    private void addUserTicket(User user, Integer ticketAmount) {
        user.ticket.addTicketCount(ticketAmount);
    }

}
