package com.kusithm.meetupd.domain.contest.entity;

import com.kusithm.meetupd.common.error.EnumNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static com.kusithm.meetupd.common.error.ErrorCode.ENUM_NOT_FOUND;

@AllArgsConstructor
@Getter
public enum ContestType {

    PLAN_IDEA(1, "기획/아이디어"),
    BRAND_NAMING(2, "브랜드/네이밍"),
    AD_MARKETING(3, "광고/마케팅"),
    IMAGE_VIDEO_UCC(4, "사진/영상/UCC"),
    DESIGN(5,"디자인"),
    ART_PHYSICAL_EDU(6, "예체능"),
    POEM(7, "문학/수기/시나리오"),
    IT(8, "IT/소프트웨어/게임"),
    STARTUP(9, "창업/스타트업"),
    ETC(10, "기타");

    private final Integer code;

    private final String value;

    public static ContestType ofCode(Integer dbData) {
        return Arrays.stream(ContestType.values())
                .filter(v -> v.getCode().equals(dbData))
                .findAny()
                .orElseThrow(() -> new EnumNotFoundException(ENUM_NOT_FOUND));
    }
}
