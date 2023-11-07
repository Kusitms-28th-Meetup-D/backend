package com.kusithm.meetupd.domain.team.entity;

import com.kusithm.meetupd.common.error.EnumNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static com.kusithm.meetupd.common.error.ErrorCode.ENUM_NOT_FOUND;


@AllArgsConstructor
@Getter
public enum TeamProgressType {

    RECRUITING(1,"모집중"),
    RECRUITMENT_COMPLETED(2,"모집완료"),
    PROCEDDING(3,"진행중"),
    PROGRESS_ENDED(4,"진행종료");

    private final Integer number;
    private final String value;

    public TeamProgressType ofCode(Integer dbData){
        return Arrays.stream(TeamProgressType.values())
                .filter(v->v.getNumber().equals(dbData))
                .findAny()
                .orElseThrow(() -> new EnumNotFoundException(ENUM_NOT_FOUND));
    }

}
