package com.wanted.preonboarding.feed.service.impl;

import com.wanted.preonboarding.feed.dto.CreateFeedDto;
import com.wanted.preonboarding.feed.dto.FeedDto;
import com.wanted.preonboarding.feed.dto.UpdateFeedDto;
import com.wanted.preonboarding.feed.entity.Feed;
import com.wanted.preonboarding.feed.repositroy.FeedRepository;
import com.wanted.preonboarding.feed.service.FeedService;
import com.wanted.preonboarding.hashtag.entity.FeedHashTag;
import com.wanted.preonboarding.hashtag.entity.Hashtag;
import com.wanted.preonboarding.hashtag.service.FeedHashTagService;
import com.wanted.preonboarding.hashtag.service.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
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

    @Override
    @Transactional
    public Page<FeedDto> feedList(Pageable pageable) {
        Page<Feed> feeds = feedRepository.findAll(pageable);
        Page<FeedDto> feedDtoPage = feeds.map(FeedDto::fromEntity);

        return feedDtoPage;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedDto> searchByTitle(String keyword, Pageable pageable) {
        Page<Feed> feedPage = feedRepository.findByTitleContaining(keyword, pageable);
        Page<FeedDto> feedDtoPage = feedPage.map(FeedDto::fromEntity);

        return feedDtoPage;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedDto> searchByContent(String keyword, Pageable pageable) {
        Page<Feed> feedPage = feedRepository.findByContentContaining(keyword, pageable);
        Page<FeedDto> feedDtoPage = feedPage.map(FeedDto::fromEntity);

        return feedDtoPage;
    }

}
