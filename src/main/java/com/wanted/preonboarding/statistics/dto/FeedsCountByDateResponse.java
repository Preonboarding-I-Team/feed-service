package com.wanted.preonboarding.statistics.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@ToString
public class FeedsCountByDateResponse {

    private final LocalDate date;
    private final Long count;

    @QueryProjection
    public FeedsCountByDateResponse(String yyyymmdd, Long count) {
        this.date = LocalDate.parse(yyyymmdd, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.count = count;
    }
}
