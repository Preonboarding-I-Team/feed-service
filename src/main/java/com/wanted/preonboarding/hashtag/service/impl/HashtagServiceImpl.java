package com.wanted.preonboarding.hashtag.service.impl;

import com.wanted.preonboarding.hashtag.entity.Hashtag;
import com.wanted.preonboarding.hashtag.repository.HashTagRepository;
import com.wanted.preonboarding.hashtag.service.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashTagRepository hashtagRepository;

    @Override
    public Hashtag createHashtag(String name) {
        Hashtag hashtag = findHashtagByName(name);
        if (hashtag == null) {
            hashtag = Hashtag.builder()
                    .name(name)
                    .build();
            hashtag = hashtagRepository.save(hashtag);
        }
        return hashtag;
    }


    @Override
    public Hashtag findHashtagByName(String name) {
        return hashtagRepository.findByName(name);
    }
}
