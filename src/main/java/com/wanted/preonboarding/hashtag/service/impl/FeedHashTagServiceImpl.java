package com.wanted.preonboarding.hashtag.service.impl;


import com.wanted.preonboarding.hashtag.entity.FeedHashTag;
import com.wanted.preonboarding.hashtag.repository.FeedHashTagRepository;
import com.wanted.preonboarding.hashtag.service.FeedHashTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedHashTagServiceImpl implements FeedHashTagService {

    private final FeedHashTagRepository feedHashTagRepository;

    @Override
    public FeedHashTag saveFeedHashTag(FeedHashTag feedHashTag) {
        return feedHashTagRepository.save(feedHashTag);
    }

}