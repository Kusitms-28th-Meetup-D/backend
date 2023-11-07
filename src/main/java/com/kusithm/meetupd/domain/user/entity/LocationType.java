package com.kusithm.meetupd.domain.user.entity;

import com.kusithm.meetupd.common.error.EnumNotFoundException;
import com.kusithm.meetupd.domain.contest.entity.ContestType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static com.kusithm.meetupd.common.error.ErrorCode.ENUM_NOT_FOUND;

@AllArgsConstructor
@Getter
public enum LocationType {
    SEOUL(1, "서울"),
    BUSAN(2, "부산"),
    DAEGU(3, "대구"),
    INCHEON(4, "인천"),
    GWANGJU(5,"광주"),
    DAEJEON(6, "대전"),
    ULSAN(7, "울산"),
    SEJEONG(8, "세종"),
    GYEONGGI(9, "경기"),
    CHUNGBUK(10, "충북"),
    CHUNGNAM(11, "충남"),
    JEONBUK(12, "전북"),
    JEONNAM(13, "전남"),
    GYEONGBUK(14, "경북"),
    GYEONGNAM(15, "경남"),
    JEJU(16, "제주"),
    GANGNAM(17
            , "강남");

    private final Integer code;

    private final String value;

    public static LocationType ofCode(Integer dbData) {
        return Arrays.stream(LocationType.values())
                .filter(v -> v.getCode().equals(dbData))
                .findAny()
                .orElseThrow(() -> new EnumNotFoundException(ENUM_NOT_FOUND));
    }
}
