package com.kusithm.meetupd.domain.review.entity.inner;

import com.kusithm.meetupd.domain.review.dto.request.UploadReviewRequestDto.SelectTeamCultureDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@Getter
public class SelectTeamCulture {

    @Field(name = "feedback_style")
    private Integer feedbackStyle;

    @Field(name = "team_style")
    private Integer teamStyle;

    @Field(name = "personality_style")
    private Integer personalityStyle;

    public static SelectTeamCulture of(SelectTeamCultureDto selectTeamCultureDto) {
        return SelectTeamCulture.builder()
                .feedbackStyle(selectTeamCultureDto.getFeedbackStyle())
                .teamStyle(selectTeamCultureDto.getTeamStyle())
                .personalityStyle(selectTeamCultureDto.getPersonalityStyle())
                .build();
    }
}
