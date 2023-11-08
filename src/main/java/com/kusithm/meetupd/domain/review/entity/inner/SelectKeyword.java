package com.kusithm.meetupd.domain.review.entity.inner;

import com.kusithm.meetupd.domain.review.dto.request.UploadReviewRequestDto;
import com.kusithm.meetupd.domain.review.dto.request.UploadReviewRequestDto.SelectKeywordDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@Getter
public class SelectKeyword {

    @Field(name = "select_keyword")
    private Integer selectKeyword;

    public static SelectKeyword of(SelectKeywordDto selectKeywordDto) {
        return SelectKeyword.builder()
                .selectKeyword(selectKeywordDto.getSelectKeyword())
                .build();
    }
}
