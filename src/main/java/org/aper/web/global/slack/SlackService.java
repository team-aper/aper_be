package org.aper.web.global.slack;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class SlackService {

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${slack.webhook.url}")
    private String webhookUrl;
    @Value("${slack.enabled}")
    private boolean slackEnabled;

    @Async
    public void sendErrorMessageToSlack(String message) {

        if (!slackEnabled) {
            return;
        }

        String payload = "{\"text\": \"" + message + "\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        log.info("Slack Webhook URL: {}", webhookUrl);

        try {
            ResponseEntity<String> response = restTemplate.exchange(webhookUrl, HttpMethod.POST, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Slack 메시지 전송 성공");
            } else {
                log.error("Slack 메시지 전송 실패: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Slack 메시지 전송 중 예외 발생: {}", e.getMessage(), e);
        }
    }
}
