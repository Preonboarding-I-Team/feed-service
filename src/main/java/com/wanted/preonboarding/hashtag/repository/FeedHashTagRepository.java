package com.wanted.preonboarding.hashtag.repository;

import com.wanted.preonboarding.feed.entity.Feed;
import com.wanted.preonboarding.hashtag.entity.FeedHashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedHashTagRepository extends JpaRepository<FeedHashTag, Long> {
    List<FeedHashTag> findByFeed(Feed feed);
}
