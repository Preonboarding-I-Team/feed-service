package com.wanted.preonboarding.hashtag.repository;

import com.wanted.preonboarding.hashtag.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashTagRepository extends JpaRepository<Hashtag, Long> {
    Hashtag findByName(String name);
}
