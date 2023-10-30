package com.wanted.preonboarding.hashtag.service;


import com.wanted.preonboarding.hashtag.entity.FeedHashTag;

public interface FeedHashTagService {
    FeedHashTag saveFeedHashTag(FeedHashTag feedHashTag);
    void deleteFeedHashTag(FeedHashTag feedHashTag);

}
