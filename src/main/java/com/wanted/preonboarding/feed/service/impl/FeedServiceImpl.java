package com.wanted.preonboarding.feed.service.impl;

import com.wanted.preonboarding.feed.dto.CreateFeedDto;
import com.wanted.preonboarding.feed.entity.Feed;
import com.wanted.preonboarding.feed.repository.FeedRepository;
import com.wanted.preonboarding.feed.service.FeedService;
import com.wanted.preonboarding.hashtag.entity.FeedHashTag;
import com.wanted.preonboarding.hashtag.service.FeedHashTagService;
import com.wanted.preonboarding.hashtag.service.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {
    private final FeedRepository feedRepository;
    private final HashtagService hashtagService;
    private final FeedHashTagService feedHashTagService;


    @Override
    @Transactional
    public Feed createFeed(CreateFeedDto createFeedDto) {
        Feed feed = createFeedDto.toEntity();

        Set<FeedHashTag> feedHashTags = createFeedDto.getHashtags().stream()
                .map(hashtagService::createHashtag)
                .map(hashtag -> FeedHashTag.builder()
                        .feed(feed)
                        .hashtag(hashtag)
                        .build())
                .collect(Collectors.toSet());

        Set<String> hashtags = createFeedDto.getHashtags();

        feed.setFeedHashTags(feedHashTags);

        for (FeedHashTag feedHashTag : feedHashTags) {
            feedHashTagService.saveFeedHashTag(feedHashTag);
        }

        return feedRepository.save(feed);
    }

}
