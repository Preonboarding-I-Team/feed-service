package com.wanted.preonboarding.hashtag.entity;

import com.wanted.preonboarding.feed.entity.Feed;
import com.wanted.preonboarding.global.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
public class FeedHashTag extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_hashtag_id")
    private Long feedHashTagId;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @ManyToOne
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

}
