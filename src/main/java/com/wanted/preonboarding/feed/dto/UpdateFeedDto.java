package com.wanted.preonboarding.feed.dto;

import com.wanted.preonboarding.feed.entity.FeedType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor
public class UpdateFeedDto {
    private String contentId;
    private String title;
    private String content;
    private FeedType type;
    private Set<String> hashtags;

    @Builder
    public UpdateFeedDto(String contentId, String title, String content, FeedType type) {
        this.contentId = contentId;
        this.title = title;
        this.content = content;
        this.type = type;
    }

}
