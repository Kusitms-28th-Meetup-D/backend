package com.kusithm.meetupd.domain.contest.controller;


import com.kusithm.meetupd.common.dto.SuccessResponse;
import com.kusithm.meetupd.common.error.EntityNotFoundException;
import com.kusithm.meetupd.domain.contest.dto.response.FindContestsResponseDto;
import com.kusithm.meetupd.domain.contest.service.ContestService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static com.kusithm.meetupd.common.error.ErrorCode.ENTITY_NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/contests")
public class ContestController {

    private final ContestService contestService;

    // 콘테스트 조회 API
    @GetMapping
    public ResponseEntity<SuccessResponse<List<FindContestsResponseDto>>> findContests (@RequestParam(value = "searchStartDate", required = false, defaultValue = "20000101") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate searchDate,
                                                                                        @RequestParam(value = "contestType", required = false, defaultValue = "0") Integer contestType
    ) {
        List<FindContestsResponseDto> allContests = contestService.findContests(searchDate, contestType);
        return SuccessResponse.of(allContests);
    }
}
