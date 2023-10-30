package com.wanted.preonboarding.feed.service.impl;

import com.wanted.preonboarding.feed.dto.CreateFeedDto;
import com.wanted.preonboarding.feed.dto.FeedResponseDto;
import com.wanted.preonboarding.feed.dto.UpdateFeedDto;
import com.wanted.preonboarding.feed.entity.Feed;
import com.wanted.preonboarding.feed.entity.FeedType;
import com.wanted.preonboarding.feed.repository.FeedRepository;
import com.wanted.preonboarding.feed.service.FeedService;
import com.wanted.preonboarding.hashtag.entity.FeedHashTag;
import com.wanted.preonboarding.hashtag.repository.FeedHashTagRepository;
import com.wanted.preonboarding.hashtag.service.FeedHashTagService;
import com.wanted.preonboarding.hashtag.service.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {
    private final FeedRepository feedRepository;
    private final HashtagService hashtagService;
    private final FeedHashTagService feedHashTagService;
    private final FeedHashTagRepository feedHashTagRepository;


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
    public Feed updateFeed(Long feedId, UpdateFeedDto updateFeedDto) {
        Feed existingFeed = feedRepository.findById(feedId)
                .orElse(null);
        if (existingFeed == null) {
            return null;
        }

        existingFeed.update(updateFeedDto.getContentId(),
                updateFeedDto.getTitle(),
                updateFeedDto.getContent(),
                updateFeedDto.getType());

        deleteFeedHashTagsByFeed(existingFeed);

        Set<FeedHashTag> feedHashTags = updateFeedDto.getHashtags().stream()
                .map(hashtagService::createHashtag)
                .map(hashtag -> FeedHashTag.builder()
                        .feed(existingFeed)
                        .hashtag(hashtag)
                        .build())
                .collect(Collectors.toSet());

        existingFeed.setFeedHashTags(feedHashTags);
        for (FeedHashTag feedHashTag : feedHashTags) {
            feedHashTagService.saveFeedHashTag(feedHashTag);
        }

        return feedRepository.save(existingFeed);
    }

    @Override
    @Transactional(readOnly = true)
    public FeedResponseDto feedDetailById(Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("해당 " + feedId + "가 존재하지 않습니다."));

        return FeedResponseDto.fromEntity(feed);
    }

    @Override
    @Transactional
    public Page<FeedResponseDto> feedList(Pageable pageable) {
        Page<Feed> feeds = feedRepository.findAll(pageable);
        Page<FeedResponseDto> feedDtoPage = feeds.map(FeedResponseDto::fromEntity);

        return feedDtoPage;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedResponseDto> searchByTitle(String keyword, Pageable pageable) {
        Page<Feed> feedPage = feedRepository.findByTitleContaining(keyword, pageable);
        Page<FeedResponseDto> feedDtoPage = feedPage.map(FeedResponseDto::fromEntity);

        return feedDtoPage;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedResponseDto> searchByContent(String keyword, Pageable pageable) {
        Page<Feed> feedPage = feedRepository.findByContentContaining(keyword, pageable);
        Page<FeedResponseDto> feedDtoPage = feedPage.map(FeedResponseDto::fromEntity);

        return feedDtoPage;
    }
    @Override
    @Transactional(readOnly = true)
    public Page<FeedResponseDto> searchByTitleOrContent(String title, String content, Pageable pageable) {
        Page<Feed> feedPage = feedRepository.findByTitleContainingOrContentContaining(title, content, pageable);

        return feedPage.map(FeedResponseDto::fromEntity);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<FeedResponseDto> getFeedsByType(FeedType type, Pageable pageable) {
        Page<Feed> feedPage = feedRepository.findByType(type, pageable);

        return feedPage.map(FeedResponseDto::fromEntity);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<FeedResponseDto> searchByHashtag(String hashtag, Pageable pageable) {
        Page<Feed> feedPage = feedRepository.findByHashtagName(hashtag, pageable);
        return feedPage.map(FeedResponseDto::fromEntity);
    }



    private void deleteFeedHashTagsByFeed(Feed feed) {
        List<FeedHashTag> feedHashTags = feedHashTagRepository.findByFeed(feed);

        feedHashTagRepository.deleteAll(feedHashTags);
    }

}