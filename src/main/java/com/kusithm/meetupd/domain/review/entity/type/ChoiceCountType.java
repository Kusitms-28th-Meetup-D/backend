package com.kusithm.meetupd.domain.review.entity.type;

import com.kusithm.meetupd.common.error.EnumNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static com.kusithm.meetupd.common.error.ErrorCode.ENUM_NOT_FOUND;

@AllArgsConstructor
@Getter
public enum ChoiceCountType {


    PLAN_MASTER(0, "계획 마스터"),
    FIRE_PASSION(1, "불타는 열정왕"),
    MAJOR_MASTER(2, "전공스킬 넘사벽"),
    MASTER_COMMUNICATION(3, "소통의 귀재"),
    LOGIC_KING(4,"논리의 왕"),
    VIBE_MAKER(5, "분위기 메이커"),
    IDEA_BANK(6, "아이디어 뱅크"),
    DETAIL_GENIUS(7, "디테일 천재"),
    BEST_TEAMWORK(8, "팀워크 최고"),
    HARD_WORKER_ICON(9, "성실의 아이콘");

    private final Integer code;

    private final String value;
    public static ChoiceCountType ofCode(Integer dbData) {
        return Arrays.stream(ChoiceCountType.values())
                .filter(v -> v.getCode().equals(dbData))
                .findAny()
                .orElseThrow(() -> new EnumNotFoundException(ENUM_NOT_FOUND));
    }
}
