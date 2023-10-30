package com.wanted.preonboarding.feed.controller;

import com.wanted.preonboarding.feed.service.FeedExtraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feeds")
public class FeedExtraController {

    private final FeedExtraService feedExtraService;

    @Autowired
    public FeedExtraController(FeedExtraService feedExtraService) {
        this.feedExtraService = feedExtraService;
    }

    @PostMapping("/share/{id}")
    public ResponseEntity<String> shareFeed( @RequestParam String type, @PathVariable Long id) {
        boolean response = feedExtraService.shareFeed(id, type);

        if (response) {
            return ResponseEntity.ok("공유 성공");
        } else {
            return ResponseEntity.badRequest().body("공유 실패");
        }
    }

    @PostMapping("/likes/{id}")
    public ResponseEntity<String> likeFeed( @RequestParam String type, @PathVariable Long id) {
        boolean response = feedExtraService.likeFeed(id, type);

        if (response) {
            return ResponseEntity.ok("공유 성공");
        } else {
            return ResponseEntity.badRequest().body("공유 실패");
        }
    }
}
