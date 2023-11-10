package com.kusithm.meetupd.domain.contest.controller;


import com.kusithm.meetupd.common.dto.SuccessResponse;
import com.kusithm.meetupd.common.dto.code.SuccessCode;
import com.kusithm.meetupd.domain.contest.dto.response.FindContestsResponseDto;
import com.kusithm.meetupd.domain.contest.dto.response.GetContestDetailInfoResponseDto;
import com.kusithm.meetupd.domain.contest.service.ContestService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/contests")
public class ContestController {

    private final ContestService contestService;

    // 카테고리 공모전 조회 API
    @GetMapping("/categories")
    public ResponseEntity<SuccessResponse<List<FindContestsResponseDto>>> findContests (@RequestParam(value = "searchStartDate", required = false, defaultValue = "20000101") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate searchDate,
                                                                                        @RequestParam(value = "contestType", required = false, defaultValue = "0") Integer contestType
    ) {
        List<FindContestsResponseDto> response = contestService.findContestsByCategory(searchDate, contestType);
        return SuccessResponse.of(SuccessCode.OK, response);
    }

    // 공모전 검색어로 조회 API
    @GetMapping("/search")
    public ResponseEntity<SuccessResponse<List<FindContestsResponseDto>>> findContests (@RequestParam String searchText) {
        List<FindContestsResponseDto> response = contestService.findContestsBySearchText(searchText);
        return SuccessResponse.of(SuccessCode.OK, response);
    }

    // 공모전 상세조회 API
    @GetMapping("/detail")
    public ResponseEntity<SuccessResponse<GetContestDetailInfoResponseDto>> getContestDetailInfo(@RequestParam String contestId) {
        GetContestDetailInfoResponseDto response = contestService.getContestDetailById(contestId);
        return SuccessResponse.of(SuccessCode.OK, response);
    }

}
