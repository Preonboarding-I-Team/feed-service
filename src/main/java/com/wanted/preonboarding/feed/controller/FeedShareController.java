package com.wanted.preonboarding.feed.controller;

import com.wanted.preonboarding.feed.dto.FeedDTO;
import com.wanted.preonboarding.feed.service.FeedShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feeds")
public class FeedShareController {

    @Autowired
    private FeedShareService feedShareService;

    @PostMapping("/share/{id}")
    public ResponseEntity<String> shareFeed( @RequestParam String type, @PathVariable Long id) {
        boolean response = feedShareService.shareFeed(id, type);

        if (response) {
            return ResponseEntity.ok("공유 성공");
        } else {
            return ResponseEntity.badRequest().body("공유 실패");
        }
    }

}
