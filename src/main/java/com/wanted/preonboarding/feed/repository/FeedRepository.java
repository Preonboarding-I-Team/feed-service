package com.wanted.preonboarding.feed.repository;

import com.wanted.preonboarding.feed.entity.Feed;
import com.wanted.preonboarding.feed.entity.FeedType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long>, FeedRepositoryCustom {
    Page<Feed> findByTitleContaining(String keyword, Pageable pageable);
    Page<Feed> findByContentContaining(String keyword, Pageable pageable);
    Page<Feed> findByType(FeedType type, Pageable pageable);

}
