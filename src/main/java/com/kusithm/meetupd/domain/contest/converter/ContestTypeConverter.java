package com.kusithm.meetupd.domain.contest.converter;

import com.kusithm.meetupd.domain.contest.entity.ContestType;
import jakarta.persistence.AttributeConverter;

public class ContestTypeConverter implements AttributeConverter<ContestType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ContestType contestType) {
        return contestType.getCode();
    }

    @Override
    public ContestType convertToEntityAttribute(Integer dbData) {
        return ContestType.ofCode(dbData);
    }
}

