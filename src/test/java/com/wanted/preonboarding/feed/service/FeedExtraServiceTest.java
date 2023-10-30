package com.wanted.preonboarding.feed.service;

import com.wanted.preonboarding.feed.entity.Feed;
import com.wanted.preonboarding.feed.repository.FeedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedExtraServiceTest {

    @Mock
    private FeedRepository feedRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private FeedExtraService feedExtraService;

    @BeforeEach
    public void setUp() {
        feedRepository = Mockito.mock(FeedRepository.class);
        restTemplate = Mockito.mock(RestTemplate.class);
        feedExtraService = new FeedExtraService(feedRepository, restTemplate);

    }

    @Test
    public void testShareFeed() {
        // given
        Long feedId = 1L;
        String type = "FACEBOOK";

        Feed feed = Feed.builder().contentId("1").build();

        when(feedRepository.findById(feedId)).thenReturn(Optional.of(feed));

        when(restTemplate.postForEntity(any(String.class), any(), any())).thenAnswer(invocation -> {
            return ResponseEntity.ok("응답 성공");
        });

        // when
        boolean result = feedExtraService.shareFeed(feedId, type);

        // then
        assertTrue(result);
        assertEquals(1,feed.getShareCount());
    }

    @Test
    public void testLikeFeed() {
        // given
        Long feedId = 1L;
        String type = "FACEBOOK";

        Feed feed = Feed.builder().contentId("1").build();

        when(feedRepository.findById(feedId)).thenReturn(Optional.of(feed));

        when(restTemplate.postForEntity(any(String.class), any(), any())).thenAnswer(invocation -> {
            return ResponseEntity.ok("응답 성공");
        });

        // when
        boolean result = feedExtraService.likeFeed(feedId, type);

        // then
        assertTrue(result);
        assertEquals(1,feed.getLikeCount());
    }
}