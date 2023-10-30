package com.wanted.preonboarding.feed.dto;

import com.wanted.preonboarding.feed.entity.Feed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedResponseDto {
    private String contentId;
    private String title;
    private String content;
    private String type;
    private Set<String> hashtags;

    public static FeedResponseDto fromEntity(Feed feed) {
        Set<String> hashtags = feed.getFeedHashTag().stream()
                .map(feedHashTag -> feedHashTag.getHashtag().getName())
                .collect(Collectors.toSet());

        return builder()
                .contentId(feed.getContentId())
                .title(feed.getTitle())
                .content(feed.getContent())
                .type(feed.getType().name())
                .hashtags(hashtags)
                .build();
    }


}