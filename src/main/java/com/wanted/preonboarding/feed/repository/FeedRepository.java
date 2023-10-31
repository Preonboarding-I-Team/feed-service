package com.wanted.preonboarding.feed.repository;

import com.wanted.preonboarding.feed.entity.Feed;
import com.wanted.preonboarding.feed.entity.FeedType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long>, FeedRepositoryCustom {
    Page<Feed> findByTitleContaining(String keyword, Pageable pageable);
    Page<Feed> findByContentContaining(String keyword, Pageable pageable);
    Page<Feed> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);
    Page<Feed> findByType(FeedType type, Pageable pageable);
    @Query("SELECT f FROM Feed f JOIN f.feedHashTag fh JOIN fh.hashtag h WHERE h.name = :hashtag")
    Page<Feed> findByHashtagName(@Param("hashtag") String hashtag, Pageable pageable);

}
