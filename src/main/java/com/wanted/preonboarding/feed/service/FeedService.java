package com.wanted.preonboarding.feed.service;

import com.wanted.preonboarding.feed.dto.CreateFeedDto;
import com.wanted.preonboarding.feed.entity.Feed;

public interface FeedService {
    Feed createFeed(CreateFeedDto createFeedDto);
}
