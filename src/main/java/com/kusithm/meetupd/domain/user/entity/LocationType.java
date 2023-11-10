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
    SEOUL(1, "서울특별시"),
    INCHEON(2, "인천광역시"),
    DAEJEON(3, "대전광역시"),
    GWANGJU(4,"광주광역시"),
    DAEGU(5, "대구광역시"),
    ULSAN(6, "울산광역시"),
    BUSAN(7, "부산광역시"),
    GYEONGGI(8, "경기도"),
    GANGWONDO(9,"강원도"),
    CHUNGBUK(10, "충청북도"),
    CHUNGNAM(11, "충청남도"),
    JEONBUK(12, "전라북도"),
    JEONNAM(13, "전라남도"),
    GYEONGBUK(14, "경상북도"),
    GYEONGNAM(15, "경상남도"),
    JEJU(16, "제주특별자치도");

    private final Integer code;

    private final String value;

    public static LocationType ofCode(Integer dbData) {
        return Arrays.stream(LocationType.values())
                .filter(v -> v.getCode().equals(dbData))
                .findAny()
                .orElseThrow(() -> new EnumNotFoundException(ENUM_NOT_FOUND));
    }
}
