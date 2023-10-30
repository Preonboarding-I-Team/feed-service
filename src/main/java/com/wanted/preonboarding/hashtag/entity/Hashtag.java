package com.wanted.preonboarding.hashtag.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hashtag")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    private Long hashTagId;
    private String name;
    @OneToMany(mappedBy = "hashtag")
    private Set<FeedHashTag> feedHashTag = new HashSet<>();

}