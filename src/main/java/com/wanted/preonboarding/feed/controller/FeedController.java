package com.wanted.preonboarding.feed.controller;

import com.wanted.preonboarding.feed.dto.CreateFeedDto;
import com.wanted.preonboarding.feed.service.FeedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;

    @PostMapping("/register")
    public ResponseEntity<String> createBoard(@RequestBody @Valid CreateFeedDto createFeedDto) {
        feedService.createFeed(createFeedDto);
        return ResponseEntity.ok("게시글이 등록되었습니다");
    }
}

