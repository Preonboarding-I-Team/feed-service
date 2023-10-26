package com.wanted.preonboarding.feed.service;


import com.wanted.preonboarding.feed.entity.Feed;
import com.wanted.preonboarding.feed.repository.FeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class FeedShareService {

    private final FeedRepository feedRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public FeedShareService(FeedRepository feedRepository, RestTemplate restTemplate) {
        this.feedRepository = feedRepository;
        this.restTemplate = restTemplate;
    }

    public boolean shareFeed(Long feedId, String type) {
        Optional<Feed> optionalFeed = feedRepository.findById(feedId);

        if (optionalFeed.isPresent()) {
            Feed feed = optionalFeed.get();

            ResponseEntity<String> response = requestExternalApi(feed.getContentId(), type);

            if (response.getStatusCode().is2xxSuccessful()) {
                feed.setShareCount(feed.getShareCount() + 1);
                feedRepository.save(feed);
                return true;
            }
        }
        return true;
    }

    private ResponseEntity<String> requestExternalApi(String contentId, String type) {
        String url = setUrl(contentId, type);

        ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
        return response;
    }

    private String setUrl(String contentId, String type) {
        if ("FACEBOOK".equalsIgnoreCase(type)) {
            return "https://www.facebook.com/share/" + contentId;
        } else if ("TWITTER".equalsIgnoreCase(type)) {
            return "https://www.twitter.com/share/" + contentId;
        } else if ("INSTAGRAM".equalsIgnoreCase(type)) {
            return "https://www.instagram.com/share/" + contentId;
        } else if ("THREADS".equalsIgnoreCase(type)) {
            return "https://www.threads.net/share/" + contentId;
        }
        return  null;
    }
}

