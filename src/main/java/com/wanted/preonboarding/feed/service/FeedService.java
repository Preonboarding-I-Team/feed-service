package com.wanted.preonboarding.feed.service;

import com.wanted.preonboarding.feed.dto.CreateFeedDto;
import com.wanted.preonboarding.feed.dto.FeedResponseDto;
import com.wanted.preonboarding.feed.dto.UpdateFeedDto;
import com.wanted.preonboarding.feed.entity.Feed;
import com.wanted.preonboarding.feed.entity.FeedType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedService {
    Feed createFeed(CreateFeedDto createFeedDto);
    Feed updateFeed(Long feedId, UpdateFeedDto updateFeedDto);
    FeedResponseDto feedDetailById(Long feedId);
    Page<FeedResponseDto> feedList(Pageable pageable);
    Page<FeedResponseDto> searchByTitle(String keyword, Pageable pageable);
    Page<FeedResponseDto> searchByContent(String keyword, Pageable pageable);
    Page<FeedResponseDto> searchByTitleOrContent(String title, String content, Pageable pageable);

    Page<FeedResponseDto> getFeedsByType(FeedType type, Pageable pageable);

}