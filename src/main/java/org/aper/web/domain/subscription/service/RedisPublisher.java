package org.aper.web.domain.subscription.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;

    @Autowired
    public RedisPublisher(RedisTemplate<String, Object> redisTemplate, ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    public void publish(Long subscriberId, Boolean hasNewEpisode) {
        String message = subscriberId + ":" + hasNewEpisode;
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
