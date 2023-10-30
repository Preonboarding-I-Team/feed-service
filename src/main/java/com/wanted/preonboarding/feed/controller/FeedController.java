package com.wanted.preonboarding.feed.controller;

import com.wanted.preonboarding.feed.dto.CreateFeedDto;
import com.wanted.preonboarding.feed.dto.FeedResponseDto;
import com.wanted.preonboarding.feed.entity.Feed;
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
    @GetMapping("/{feedId}")
    public ResponseEntity<FeedResponseDto> getFeedDetails(@PathVariable Long feedId) {
        FeedResponseDto feedDto = feedService.feedDetailById(feedId);
        return ResponseEntity.ok(feedDto);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<FeedResponseDto>> feedList(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<FeedResponseDto> feedPage = feedService.feedList(pageable);
        return new ResponseEntity<>(feedPage, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<FeedResponseDto>> searchFeeds(
            @RequestParam String search_by,
            @RequestParam String keyword,
            Pageable pageable
    ) {
        Page<FeedResponseDto> feedPage;

        if ("title".equalsIgnoreCase(search_by)) {
            feedPage = feedService.searchByTitle(keyword, pageable);
        } else if ("content".equalsIgnoreCase(search_by)) {
            feedPage = feedService.searchByContent(keyword, pageable);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(feedPage, HttpStatus.OK);
    }


    @PutMapping("/{feedId}")
    public ResponseEntity<Feed> updateFeed(@PathVariable Long feedId, @RequestBody @Valid UpdateFeedDto updateFeedDto) {
        Feed updatedFeed = feedService.updateFeed(feedId, updateFeedDto);
        return new ResponseEntity<>(updatedFeed, HttpStatus.OK);
    }
}

