package com.kusithm.meetupd.domain.team.entity;

import com.kusithm.meetupd.common.error.EnumNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static com.kusithm.meetupd.common.error.ErrorCode.ENUM_NOT_FOUND;

@AllArgsConstructor
@Getter
public enum TeamUserRoleType {

    TEAM_LEADER(1, "팀장"),
    TEAM_MEMBER(2, "팀원"),
    PASS(3, "합격자"),
    FAILED(4, "반려자"),
    VOLUNTEER(5, "지원자");

    private final Integer code;
    private final String value;

    public static TeamUserRoleType ofCode(int dbData) {
        return Arrays.stream(TeamUserRoleType.values())
                .filter(v -> v.getCode().equals(dbData))
                .findAny()
                .orElseThrow(() -> new EnumNotFoundException(ENUM_NOT_FOUND));
    }


}
