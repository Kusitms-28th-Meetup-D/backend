package com.kusithm.meetupd.domain.recommendation.entity.type;

import com.kusithm.meetupd.common.error.EnumNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static com.kusithm.meetupd.common.error.ErrorCode.ENUM_NOT_FOUND;

@AllArgsConstructor
@Getter
public enum WorkMethodType {


    WORK_STYLE(0, "작업 스타일"),
    RESULT_PROCESS(1, "결과, 과정 어디에 중점"),
    WORK_LIFE_BALANCE(2, "추구하는 워라벨");
    private final Integer code;

    private final String value;
    public static WorkMethodType ofCode(Integer dbData) {
        return Arrays.stream(WorkMethodType.values())
                .filter(v -> v.getCode().equals(dbData))
                .findAny()
                .orElseThrow(() -> new EnumNotFoundException(ENUM_NOT_FOUND));
    }
}
