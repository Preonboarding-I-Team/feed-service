package com.wanted.preonboarding.feed.controller;

import com.wanted.preonboarding.feed.dto.CreateFeedDto;
import com.wanted.preonboarding.feed.dto.FeedDto;
import com.wanted.preonboarding.feed.dto.UpdateFeedDto;
import com.wanted.preonboarding.feed.entity.Feed;
import com.wanted.preonboarding.feed.service.FeedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{feedId}")
    public ResponseEntity<FeedDto> getFeedDetails(@PathVariable Long feedId) {
        FeedDto feedDto = feedService.feedDetailById(feedId);
        return ResponseEntity.ok(feedDto);
    }
}

