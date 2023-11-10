package com.kusithm.meetupd.domain.team.dto.request;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.awt.print.Pageable;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class PageDto {

    private int page;
    private int size;

    public PageDto() {
        this.page = 1;
        this.size = 10;
    }
}
