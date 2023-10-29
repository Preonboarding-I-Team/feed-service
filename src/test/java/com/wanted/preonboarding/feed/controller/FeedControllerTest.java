package com.wanted.preonboarding.feed.controller;

import com.wanted.preonboarding.feed.service.FeedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeedControllerTest {

    @Mock
    private FeedService feedService;

    private FeedController feedController;

    @BeforeEach
    public void setUp() {
        feedService = Mockito.mock(FeedService.class);
        feedController = new FeedController(feedService);
    }

    @Test
    public void testShareFeed() {
        // given
        Long feedId = 1L;
        String type = "FASCEBOOK";

        when(feedService.shareFeed(feedId, type)).thenReturn(true);

        // when
        ResponseEntity<String> response = feedController.shareFeed(type, feedId);

        // then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("공유 성공", response.getBody());
    }
}