package com.kusithm.meetupd.domain.review.entity.type;

import com.kusithm.meetupd.common.error.EnumNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static com.kusithm.meetupd.common.error.ErrorCode.ENUM_NOT_FOUND;

@AllArgsConstructor
@Getter
public enum TeamCultureType {


    FEEDBACK_STYLE(0, "피드백 스타일"),
    TEAM_STYLE(1, "추구하는 팀 스타일"),
    PERSONALITY_STYLE(2, "외향적, 내향적");
    private final Integer code;

    private final String value;
    public static TeamCultureType ofCode(Integer dbData) {
        return Arrays.stream(TeamCultureType.values())
                .filter(v -> v.getCode().equals(dbData))
                .findAny()
                .orElseThrow(() -> new EnumNotFoundException(ENUM_NOT_FOUND));
    }
}
