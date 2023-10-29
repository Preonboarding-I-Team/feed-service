package com.wanted.preonboarding.feed.repository;

import com.wanted.preonboarding.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
}
