package com.wanted.preonboarding.hashtag.service;


import com.wanted.preonboarding.hashtag.entity.Hashtag;

public interface HashtagService {
    Hashtag createHashtag(String name);
    Hashtag findHashtagByName(String name);
}