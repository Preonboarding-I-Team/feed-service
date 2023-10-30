package com.wanted.preonboarding.statistics.dto;

import com.wanted.preonboarding.statistics.SortType;
import com.wanted.preonboarding.statistics.SortValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StatisticsRequestTest {

    @Test
    @DisplayName("생성자 테스트")
    void constructor() {
        LocalDate now = LocalDate.now();
        StatisticsRequest request = new StatisticsRequest("danny", SortType.DATE, SortValue.COUNT, now, now);
        assertThat(request).isNotNull();
    }

    @Test
    @DisplayName("Getter 테스트")
    void getter() {
        LocalDate start = LocalDate.now().minusDays(2L);
        LocalDate end = LocalDate.now();
        StatisticsRequest request = new StatisticsRequest("danny", SortType.DATE, SortValue.COUNT, start, end);

        assertThat(request.getHashtag()).isEqualTo("danny");
        assertThat(request.getSortType()).isSameAs(SortType.DATE);
        assertThat(request.getSortValue()).isSameAs(SortValue.COUNT);
        assertThat(request.getStart()).isEqualTo(start.atStartOfDay());
        assertThat(request.getEnd()).isEqualTo(end.atStartOfDay());
    }

    @Test
    @DisplayName("When Start Date: null -> 기본값 7일전 테스트")
    void when_start_date_null() {
        LocalDate now = LocalDate.now();
        StatisticsRequest request = new StatisticsRequest("danny", SortType.DATE, SortValue.COUNT, null, now);

        assertThat(request.getStart()).isEqualTo(now.minusDays(7L).atStartOfDay());
    }

    @Test
    @DisplayName("When End Date: null -> 기본값 당일 테스트")
    void when_end_date_null() {
        LocalDate now = LocalDate.now();
        StatisticsRequest request = new StatisticsRequest("danny", SortType.DATE, SortValue.COUNT, now, null);

        assertThat(request.getStart()).isEqualTo(now.atStartOfDay());
    }

    @Test
    @DisplayName("When Start Date: 서비스 제공 유효범위와 맞지않을때 예외처리")
    void when_start_date_include_non_service_day_throw_exception() {
        LocalDate now = LocalDate.of(1977, Month.APRIL, 17);
        assertThatThrownBy(() -> new StatisticsRequest("apple2", SortType.DATE, SortValue.COUNT, now, now))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("When Start Date: 시작일이 오늘보다 이후일때 예외처리")
    void when_start_date_is_after_today_throw_exception() {
        LocalDate now = LocalDate.now();
        LocalDate tomorrow = now.plusDays(1L);
        assertThatThrownBy(() -> new StatisticsRequest("apple2", SortType.DATE, SortValue.COUNT, tomorrow, now))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("When Start Date: 시작일이 종료일보다 이후일때 예외처리")
    void when_start_date_is_after_end_throw_exception() {
        LocalDate now = LocalDate.now();
        LocalDate yesterday = now.minusDays(1L);
        assertThatThrownBy(() -> new StatisticsRequest("apple2", SortType.DATE, SortValue.COUNT, now, yesterday))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("When Start Date: 종료일이 오늘 이후일때 예외처리")
    void when_end_date_is_after_today_throw_exception() {
        LocalDate now = LocalDate.now();
        LocalDate tomorrow = now.plusDays(1L);
        assertThatThrownBy(() -> new StatisticsRequest("apple2", SortType.DATE, SortValue.COUNT, now, tomorrow))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("When Start & End Date: 시작일과 종료일 사이 기간이 30일이 초과할때 예외처리")
    void when_start_date_between_end_date_up_to_30days_throw_exception() {
        LocalDate start = LocalDate.of(2022, Month.JANUARY, 1);
        LocalDate end = LocalDate.of(2022, Month.JANUARY, 31);
        assertThatThrownBy(() -> new StatisticsRequest("apple2", SortType.DATE, SortValue.COUNT, start, end))
                .isInstanceOf(IllegalArgumentException.class);
    }
}