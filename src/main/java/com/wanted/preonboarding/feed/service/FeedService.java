package com.wanted.preonboarding.feed.service;

import com.wanted.preonboarding.feed.dto.CreateFeedDto;
import com.wanted.preonboarding.feed.dto.FeedResponseDto;
import com.wanted.preonboarding.feed.dto.UpdateFeedDto;
import com.wanted.preonboarding.feed.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedService {
    Feed createFeed(CreateFeedDto createFeedDto);
    FeedResponseDto feedDetailById(Long feedId);
    Page<FeedResponseDto> feedList(Pageable pageable);

    Page<FeedResponseDto> searchByTitle(String keyword, Pageable pageable);
    Page<FeedResponseDto> searchByContent(String keyword, Pageable pageable);
    Feed updateFeed(Long feedId, UpdateFeedDto updateFeedDto);
}