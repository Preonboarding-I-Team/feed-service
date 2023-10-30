package com.wanted.preonboarding.statistics.controller;

import com.wanted.preonboarding.security.user.UserPrincipal;
import com.wanted.preonboarding.statistics.SortType;
import com.wanted.preonboarding.statistics.SortValue;
import com.wanted.preonboarding.statistics.dto.StatisticsRequest;
import com.wanted.preonboarding.statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feeds/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    public <T> ResponseEntity<List<T>> getStatistics(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                     @RequestParam(value = "hashtag", required = false) String hashtag,
                                                     @RequestParam(value = "type") SortType type,
                                                     @RequestParam(value = "value", required = false, defaultValue = "count") String value,
                                                     @RequestParam(value = "start", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
                                                     @RequestParam(value = "end", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {

        String account = userPrincipal.getUsername();
        if(hashtag == null || hashtag.trim().isEmpty()) {
            hashtag = account;
        }
        StatisticsRequest request = new StatisticsRequest(hashtag, type, SortValue.valueOf(value.toUpperCase()), start, end);
        List<T> response = statisticsService.executeStatistics(request);
        return ResponseEntity.ok(response);
    }
}