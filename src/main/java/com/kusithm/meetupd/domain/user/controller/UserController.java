package com.kusithm.meetupd.domain.user.controller;

import com.kusithm.meetupd.common.auth.UserId;
import com.kusithm.meetupd.common.dto.SuccessResponse;
import com.kusithm.meetupd.common.dto.code.SuccessCode;
import com.kusithm.meetupd.domain.user.dto.UserMypageResponseDto;
import com.kusithm.meetupd.domain.user.dto.response.UserCheckResponseDto;
import com.kusithm.meetupd.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/check")
    public ResponseEntity<SuccessResponse<UserCheckResponseDto>> getUserName(@UserId Long userId) {
        UserCheckResponseDto response = userService.getUserByToken(userId);
        return SuccessResponse.of(SuccessCode.OK, response);
    }

    @GetMapping("/myPage")
    public ResponseEntity<SuccessResponse<UserMypageResponseDto>> getMypageUser(@RequestParam Long userId) {
        UserMypageResponseDto response = userService.getMypageUser(userId);
        return SuccessResponse.of(SuccessCode.OK, response);
    }


}
