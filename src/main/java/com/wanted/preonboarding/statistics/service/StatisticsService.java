package com.wanted.preonboarding.statistics.service;

import com.wanted.preonboarding.feed.repository.FeedRepository;
import com.wanted.preonboarding.statistics.SortType;
import com.wanted.preonboarding.statistics.SortValue;
import com.wanted.preonboarding.statistics.dto.HashtagsCountResponse;
import com.wanted.preonboarding.statistics.dto.StatisticsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class StatisticsService {

    private final FeedRepository feedRepository;

    public <T> List<T> executeStatistics(StatisticsRequest request) {
        SortValue sortValue = request.getSortValue();
        if (sortValue == SortValue.LIKE_COUNT || sortValue == SortValue.SHARE_COUNT || sortValue == SortValue.VIEW_COUNT) {
            Integer count = feedRepository.statisticFeedsSumBySortValue(request);
            return (List<T>) List.of(new HashtagsCountResponse(request.getHashtag(), count));
        }

        if (request.getSortType() == SortType.DATE) {
            return (List<T>) feedRepository.statisticFeedsCountContainHashtagByDate(request);
        }
        if (request.getSortType() == SortType.HOUR) {
            return (List<T>) feedRepository.statisticFeedsCountContainHashtagByHour(request);
        }
        throw new RuntimeException("no case!");
    }
}
