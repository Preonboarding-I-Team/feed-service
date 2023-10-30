package com.wanted.preonboarding.statistics.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@ToString
public class FeedsCountByDateHourResponse {

    private final LocalDateTime datetime;
    private final Long count;

    @QueryProjection
    public FeedsCountByDateHourResponse(String yyyyMMddHH, Long count) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00");
        this.datetime = LocalDateTime.parse(yyyyMMddHH, formatter);
        this.count = count;
    }
}
