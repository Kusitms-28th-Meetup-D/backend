package com.kusithm.meetupd.domain.user.service;

import com.kusithm.meetupd.common.error.EntityNotFoundException;
import com.kusithm.meetupd.common.error.ErrorCode;
import com.kusithm.meetupd.domain.user.dto.UserMypageResponseDto;
import com.kusithm.meetupd.domain.user.dto.response.UserCheckResponseDto;
import com.kusithm.meetupd.domain.user.entity.*;
import com.kusithm.meetupd.domain.user.mysql.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private User getUserByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
        return user;
    }

    public UserMypageResponseDto getMypageUser(Long userId) {
        User findUser = getUserByUserId(userId);
        return new UserMypageResponseDto(findUser);
    }
}
