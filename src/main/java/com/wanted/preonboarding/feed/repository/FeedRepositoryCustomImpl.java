package com.wanted.preonboarding.feed.repository;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanted.preonboarding.feed.entity.QFeed;
import com.wanted.preonboarding.hashtag.entity.QFeedHashTag;
import com.wanted.preonboarding.hashtag.entity.QHashtag;
import com.wanted.preonboarding.statistics.SortValue;
import com.wanted.preonboarding.statistics.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryCustomImpl implements FeedRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QFeed feed = QFeed.feed;
    QHashtag hashTag = QHashtag.hashtag;
    QFeedHashTag feedHashTag = QFeedHashTag.feedHashTag;

    @Override
    public List<FeedsCountByDateResponse> statisticFeedsCountContainHashtagByDate(StatisticsRequest request) {
        StringTemplate formattedDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                feedHashTag.createdAt,
                ConstantImpl.create("%Y-%m-%d"));

        return queryFactory.select(new QFeedsCountByDateResponse(formattedDate, feedHashTag.count()))
                .from(feedHashTag)
                .where(hashTag.name.eq(request.getHashtag())
                        .and(feedHashTag.createdAt
                                .between(request.getStart(), request.getEnd().plusDays(1L))))
                .join(hashTag).on(feedHashTag.hashtag.eq(hashTag))
                .groupBy(formattedDate)
                .orderBy(formattedDate.desc())
                .fetch();
    }

    @Override
    public List<FeedsCountByDateHourResponse> statisticFeedsCountContainHashtagByHour(StatisticsRequest request) {
        StringTemplate formattedDateHour = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                feedHashTag.createdAt,
                ConstantImpl.create("%Y-%m-%d %H:00"));

        return queryFactory.select(new QFeedsCountByDateHourResponse(formattedDateHour, feedHashTag.count()))
                .from(feedHashTag)
                .where(hashTag.name.eq(request.getHashtag())
                        .and(feedHashTag.createdAt
                                .between(request.getStart(), request.getEnd().plusDays(1L))))
                .join(hashTag).on(feedHashTag.hashtag.eq(hashTag))
                .groupBy(formattedDateHour)
                .orderBy(formattedDateHour.desc())
                .fetch();
    }

    @Override
    public Integer statisticFeedsSumBySortValue(StatisticsRequest request) {
        NumberExpression<Integer> selectedField = getSelectedField(request.getSortValue(), feed);
        return queryFactory.select(selectedField.sum())
                .from(feed)
                .join(feedHashTag).on(feedHashTag.feed.eq(feed))
                .join(hashTag).on(feedHashTag.hashtag.eq(hashTag))
                .where(hashTag.name.eq(request.getHashtag()))
                .fetchFirst();
    }

    private NumberExpression<Integer> getSelectedField(SortValue sortValue, QFeed qFeed) {
        switch (sortValue) {
            case VIEW_COUNT -> {
                return qFeed.viewCount;
            }
            case LIKE_COUNT -> {
                return qFeed.likeCount;
            }
            case SHARE_COUNT -> {
                return qFeed.shareCount;
            }
            default -> throw new IllegalArgumentException("invalid sort value");
        }
    }
}
