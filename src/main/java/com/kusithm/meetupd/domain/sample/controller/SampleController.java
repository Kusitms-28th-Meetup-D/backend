package com.kusithm.meetupd.domain.sample.controller;

import com.kusithm.meetupd.common.dto.SuccessResponse;
import com.kusithm.meetupd.common.dto.code.SuccessCode;
import com.kusithm.meetupd.domain.sample.dto.request.CreateSampleRequestDto;
import com.kusithm.meetupd.domain.sample.dto.response.CreateSampleResponseDto;
import com.kusithm.meetupd.domain.sample.service.SampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/samples")
public class SampleController {

    private final SampleService sampleService;

    @PostMapping
    public ResponseEntity<SuccessResponse<CreateSampleResponseDto>> createSample(@RequestBody CreateSampleRequestDto createSample) {
        CreateSampleResponseDto response = sampleService.createSample(createSample.getText());
        return SuccessResponse.of(SuccessCode.CREATED, response);
    }
}
