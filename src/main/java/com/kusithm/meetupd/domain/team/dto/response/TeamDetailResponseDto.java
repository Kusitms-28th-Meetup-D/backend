package com.kusithm.meetupd.domain.team.dto.response;

import java.util.Date;
import java.util.List;

public class TeamDetailResponseDto {

    public ResponseTeamLeaderDto leaderInfo;

    public int expectedMember;//모집할 인원수
    public int joinMember;//합류한 인원수
    public String location;//활동지역
    public String endDate;//활동 종료 예정일
    public String notice;//모집공고
    public int leftMember;//남은 자리수 : 모집할 인원수 - 모집된 인원수
    public List<ResponseTeamMemeberDto> teamMemeberInfos;
}
