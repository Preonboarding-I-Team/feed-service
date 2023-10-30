package com.wanted.preonboarding.feed.service;

import com.wanted.preonboarding.feed.entity.Feed;
import com.wanted.preonboarding.feed.repository.FeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class FeedExtraService {

    private final FeedRepository feedRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public FeedExtraService(FeedRepository feedRepository, RestTemplate restTemplate) {
        this.feedRepository = feedRepository;
        this.restTemplate = restTemplate;
    }

    public boolean shareFeed(Long feedId, String type) {
        Optional<Feed> optionalFeed = feedRepository.findById(feedId);

        String buttonType = "share";

        if (optionalFeed.isPresent()) {
            Feed feed = optionalFeed.get();

            ResponseEntity<String> response = requestExternalApi(feed.getContentId(), type, buttonType);

            if (response.getStatusCode().is2xxSuccessful()) {
                feed.incrementShareCount();
                feedRepository.save(feed);
                return true;
            }
        }
        return false; //예외 처리
    }

    public boolean likeFeed(Long feedId, String type) {
        Optional<Feed> optionalFeed = feedRepository.findById(feedId);

        String buttonType = "likes";

        if (optionalFeed.isPresent()) {
            Feed feed = optionalFeed.get();

            ResponseEntity<String> response = requestExternalApi(feed.getContentId(), type, buttonType);

            if (response.getStatusCode().is2xxSuccessful()) {
                feed.incrementLikeCount();
                feedRepository.save(feed);
                return true;
            }
        }
        return false; //예외 처리
    }

    private ResponseEntity<String> requestExternalApi(String contentId, String type, String buttonType) {
        String url = setUrl(contentId, type, buttonType);

        ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);

        System.out.println("url : "+url);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        } else {
            // WARN: 추후 키 설정 이후에 정상 동작 예정. 배포 전 삭제 요망.
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        }
    }

    private String setUrl(String contentId, String type, String buttonType) {
        if ("FACEBOOK".equalsIgnoreCase(type)) {
            return "https://www.facebook.com/" + buttonType + "/" + contentId;
        } else if ("TWITTER".equalsIgnoreCase(type)) {
            return "https://www.twitter.com/" + buttonType + "/" + contentId;
        } else if ("INSTAGRAM".equalsIgnoreCase(type)) {
            return "https://www.instagram.com/" + buttonType + "/" + contentId;
        } else if ("THREADS".equalsIgnoreCase(type)) {
            return "https://www.threads.net/" + buttonType + "/" + contentId;
        }
        return null;
    }
}

