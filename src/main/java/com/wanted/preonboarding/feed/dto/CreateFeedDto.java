package com.wanted.preonboarding.feed.dto;

import com.wanted.preonboarding.feed.entity.Feed;
import com.wanted.preonboarding.feed.entity.FeedType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFeedDto {

    private String contentId;
    private String title;
    private String content;
    private FeedType type;
    private Set<String> hashtags;

    public Feed toEntity() {
        return Feed.builder()
                .contentId(contentId)
                .title(title)
                .content(content)
                .type(type)
                .build();
    }

}