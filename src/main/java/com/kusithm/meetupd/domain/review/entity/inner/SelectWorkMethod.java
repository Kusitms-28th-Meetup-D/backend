package com.kusithm.meetupd.domain.review.entity.inner;


import com.kusithm.meetupd.domain.review.dto.request.UploadReviewRequestDto.SelectWorkMethodDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@Getter
public class SelectWorkMethod {

    @Field(name = "work_style")
    private Integer workStyle;

    @Field(name = "result_process")
    private Integer resultProcess;

    @Field(name = "work_life_balance")
    private Integer workLifeBalance;

    public static SelectWorkMethod of(SelectWorkMethodDto selectWorkMethodDto) {
        return SelectWorkMethod.builder()
                .workStyle(selectWorkMethodDto.getWorkStyle())
                .resultProcess(selectWorkMethodDto.getResultProcess())
                .workLifeBalance(selectWorkMethodDto.getWorkLifeBalance())
                .build();
    }
}
