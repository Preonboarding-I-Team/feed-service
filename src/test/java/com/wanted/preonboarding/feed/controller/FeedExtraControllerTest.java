package com.wanted.preonboarding.feed.controller;

import com.wanted.preonboarding.feed.service.FeedExtraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeedExtraControllerTest {

    @Mock
    private FeedExtraService feedExtraService;

    private FeedExtraController feedExtraController;

    @BeforeEach
    public void setUp() {
        feedExtraService = Mockito.mock(FeedExtraService.class);
        feedExtraController = new FeedExtraController(feedExtraService);
    }

    @Test
    public void testShareFeed() {
        // given
        Long feedId = 1L;
        String type = "FASCEBOOK";

        when(feedExtraService.shareFeed(feedId, type)).thenReturn(true);

        // when
        ResponseEntity<String> response = feedExtraController.shareFeed(type, feedId);

        // then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("공유 성공", response.getBody());
    }

    @Test
    public void testLikeFeed() {
        // given
        Long feedId = 1L;
        String type = "FASCEBOOK";

        when(feedExtraService.likeFeed(feedId, type)).thenReturn(true);

        // when
        ResponseEntity<String> response = feedExtraController.likeFeed(type, feedId);

        // then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("공유 성공", response.getBody());
    }
}