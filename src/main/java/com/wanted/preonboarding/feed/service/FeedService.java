package com.wanted.preonboarding.feed.service;

import com.wanted.preonboarding.feed.dto.CreateFeedDto;
import com.wanted.preonboarding.feed.dto.FeedDto;
import com.wanted.preonboarding.feed.dto.UpdateFeedDto;
import com.wanted.preonboarding.feed.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedService {
    Feed createFeed(CreateFeedDto createFeedDto);
    Page<FeedDto> feedList(Pageable pageable);
    Page<FeedDto> searchByTitle(String keyword, Pageable pageable);
    Page<FeedDto> searchByContent(String keyword, Pageable pageable);
}
