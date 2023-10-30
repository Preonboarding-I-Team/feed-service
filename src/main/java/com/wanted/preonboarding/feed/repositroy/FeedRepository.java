package com.wanted.preonboarding.feed.repositroy;

import com.wanted.preonboarding.feed.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    Page<Feed> findByTitleContaining(String keyword, Pageable pageable);
    Page<Feed> findByContentContaining(String keyword, Pageable pageable);
}
