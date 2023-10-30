package com.wanted.preonboarding.feed.controller;

import com.wanted.preonboarding.feed.dto.CreateFeedDto;
import com.wanted.preonboarding.feed.dto.FeedResponseDto;
import com.wanted.preonboarding.feed.dto.UpdateFeedDto;
import com.wanted.preonboarding.feed.entity.Feed;
import com.wanted.preonboarding.feed.entity.FeedType;
import com.wanted.preonboarding.feed.service.FeedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    @PutMapping("/{feedId}")
    public ResponseEntity<Feed> updateFeed(@PathVariable Long feedId, @RequestBody @Valid UpdateFeedDto updateFeedDto) {
        Feed updatedFeed = feedService.updateFeed(feedId, updateFeedDto);
        return new ResponseEntity<>(updatedFeed, HttpStatus.OK);
    }
    @GetMapping("/{feedId}")
    public ResponseEntity<FeedResponseDto> getFeedDetails(@PathVariable Long feedId) {
        FeedResponseDto feedResponseDto = feedService.feedDetailById(feedId);
        return ResponseEntity.ok(feedResponseDto);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<FeedResponseDto>> feedList(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<FeedResponseDto> feedPage = feedService.feedList(pageable);
        return new ResponseEntity<>(feedPage, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<FeedResponseDto>> searchFeeds(
            @RequestParam String by,
            @RequestParam String keyword,
            Pageable pageable
    ) {
        Page<FeedResponseDto> feedPage;

        if ("title".equalsIgnoreCase(by)) {
            feedPage = feedService.searchByTitle(keyword, pageable);
        } else if ("content".equalsIgnoreCase(by)) {
            feedPage = feedService.searchByContent(keyword, pageable);
        } else if ("title-or-content".equalsIgnoreCase(by)) {
            feedPage = feedService.searchByTitleOrContent(keyword, keyword, pageable);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(feedPage, HttpStatus.OK);
    }
    @GetMapping("/search-hashtag")
    public ResponseEntity<Page<FeedResponseDto>> searchByHashtag(
            @RequestParam String hashtag,
            Pageable pageable
    ) {
        Page<FeedResponseDto> feedPage = feedService.searchByHashtag(hashtag, pageable);
        return new ResponseEntity<>(feedPage, HttpStatus.OK);
    }

    @GetMapping("/search-type")
    public ResponseEntity<Page<FeedResponseDto>> searchByType(
            @RequestParam(name = "by", required = false, defaultValue = "") FeedType type,
            Pageable pageable
    ) {
        if (type == null) {
            return new ResponseEntity<>(feedService.feedList(pageable), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(feedService.getFeedsByType(type, pageable), HttpStatus.OK);
        }
    }

}