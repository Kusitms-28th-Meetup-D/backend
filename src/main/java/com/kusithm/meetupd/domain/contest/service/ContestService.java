package com.kusithm.meetupd.domain.contest.service;


import com.kusithm.meetupd.common.error.EntityNotFoundException;
import com.kusithm.meetupd.domain.contest.dto.response.FindContestsResponseDto;
import com.kusithm.meetupd.domain.contest.entity.Contest;
import com.kusithm.meetupd.domain.contest.mongo.ContestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.kusithm.meetupd.common.error.ErrorCode.CONTEST_NOT_FOUND;

@RequiredArgsConstructor
@Transactional
@Service
public class ContestService {

    private final ContestRepository contestRepository;


    public List<FindContestsResponseDto> findContests(LocalDate nowDate, Integer contestType) {
        // 카테고리 전체 조회일 때
        if(isFindAllContest(contestType)) {
            return findAllContests(nowDate);
        }
        else {
            return findContestsByType(nowDate, contestType);
        }
    }

    private List<FindContestsResponseDto> findAllContests(LocalDate nowDate) {
        List<Contest> findContests = findContestsByDate(nowDate);
        return makeContestResponseList(findContests, nowDate);
    }

    private List<FindContestsResponseDto> findContestsByType(LocalDate nowDate, Integer contestType) {
        List<Contest> findContests = findTypeContestsByDate(nowDate, contestType);

        return makeContestResponseList(findContests, nowDate);
    }

    private List<Contest> findContestsByDate(LocalDate nowDate) {
        List<Contest> contests = contestRepository.findAllContestsByDate(nowDate);
        if(contests.isEmpty()) {
            throw new EntityNotFoundException(CONTEST_NOT_FOUND);
        }
        return contests;
    }


    private List<Contest> findTypeContestsByDate(LocalDate nowDate, Integer contentType) {
        List<Contest> contests = contestRepository.findContestsByDateAndType(nowDate, contentType);
        if(contests.isEmpty()) {
            throw new EntityNotFoundException(CONTEST_NOT_FOUND);
        }
        return contests;
    }

    private List<FindContestsResponseDto> makeContestResponseList(List<Contest> findContests, LocalDate nowDate) {
        return findContests.stream()
                .map(data -> FindContestsResponseDto.of(data, nowDate))
                .collect(Collectors.toList());
    }

    private Boolean isFindAllContest(Integer contestType){
        return contestType.equals(0);
    }
}
