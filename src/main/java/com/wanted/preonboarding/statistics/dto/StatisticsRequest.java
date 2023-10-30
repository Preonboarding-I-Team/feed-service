package com.wanted.preonboarding.statistics.dto;

import com.wanted.preonboarding.statistics.SortType;
import com.wanted.preonboarding.statistics.SortValue;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;

@Getter
@ToString
@Slf4j
public class StatisticsRequest {

    private final String hashtag;

    private final SortType sortType;

    private final SortValue sortValue;

    private final LocalDateTime start;

    private final LocalDateTime end;

    public StatisticsRequest(String hashtag, SortType sortType, SortValue sortValue, LocalDate start, LocalDate end) {
        this.hashtag = hashtag;
        this.sortType = sortType;
        this.sortValue = sortValue;
        LocalDate now = LocalDate.now();
        if (start == null) {
            start = now.minusDays(7L);
        }
        if (end == null) {
            end = now;
        }
        this.start = start.atStartOfDay();
        this.end = end.atStartOfDay();
        validateDate(now, start, end);
    }
    
    private void validateDate(LocalDate now, LocalDate start, LocalDate end) {
        LocalDate dayOfServiceStart = LocalDate.of(2020, Month.JANUARY, 2);
        if(start.isBefore(dayOfServiceStart) || end.isBefore(dayOfServiceStart)) {
            throw new IllegalArgumentException("non service day");
        }
        if(start.isAfter(now)) {
            throw new IllegalArgumentException("start can't after now");
        }
        if(start.isAfter(end)) {
            throw new IllegalArgumentException("start can't after end");
        }
        if(end.isAfter(now)){
            throw new IllegalArgumentException("end can't after now");
        }

        long period = ChronoUnit.DAYS.between(start, end);
        if(period >= 30) {
            throw new IllegalArgumentException("period is up to 30 days");
        }
    }
}