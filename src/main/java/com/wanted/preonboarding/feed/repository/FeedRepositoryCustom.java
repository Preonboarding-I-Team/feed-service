package com.wanted.preonboarding.feed.repository;

import com.wanted.preonboarding.statistics.dto.FeedsCountByDateHourResponse;
import com.wanted.preonboarding.statistics.dto.FeedsCountByDateResponse;
import com.wanted.preonboarding.statistics.dto.StatisticsRequest;

import java.util.List;

public interface FeedRepositoryCustom {
    List<FeedsCountByDateResponse> statisticFeedsCountContainHashtagByDate(StatisticsRequest request);
    List<FeedsCountByDateHourResponse> statisticFeedsCountContainHashtagByHour(StatisticsRequest request);
    Integer statisticFeedsSumBySortValue(StatisticsRequest request);
}
