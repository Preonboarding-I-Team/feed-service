package com.wanted.preonboarding.feed.entity;

import com.wanted.preonboarding.global.entity.BaseEntity;
import com.wanted.preonboarding.hashtag.entity.FeedHashTag;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "feed")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Feed extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long feedId;

    @Column(name = "content_id")
    private String contentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private FeedType type;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "view_count", columnDefinition = "integer default 0")
    private int viewCount;

    @Column(name = "like_count", columnDefinition = "integer default 0")
    private int likeCount;

    @Column(name = "share_count", columnDefinition = "integer default 0")
    private int shareCount;

    @OneToMany(mappedBy = "feed")
    private Set<FeedHashTag> feedHashTag = new HashSet<>();

    public void increseShareCount() {
        this.shareCount++;
    }
}
