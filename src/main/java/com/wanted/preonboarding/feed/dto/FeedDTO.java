package com.wanted.preonboarding.feed.dto;

import com.wanted.preonboarding.feed.entity.FeedType;
import lombok.Data;

@Data
public class FeedDTO {
    private Long feedId;
    private String contentId;
    private FeedType type;
    private String title;
    private String content;
    private int viewCount;
    private int likeCount;
    private int shareCount;
}
