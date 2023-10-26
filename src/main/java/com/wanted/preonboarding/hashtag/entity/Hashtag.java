package com.wanted.preonboarding.hashtag.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hashtag")
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    private Long hashTagId;
    private String name;
    @OneToMany(mappedBy = "hashtag")
    private Set<FeedHashTag> boardHashTag = new HashSet<>();

}