package org.aper.web.global.sse.service;

import org.aper.web.global.sse.emitter.SseEmitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class RedisSubscriber implements MessageListener {

    private final SseEmitterService sseEmitterService;

    @Autowired
    public RedisSubscriber(SseEmitterService sseEmitterService) {
        this.sseEmitterService = sseEmitterService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String messageStr = new String(message.getBody());
        String[] parts = messageStr.split(":");
        Long subscriberId = Long.valueOf(parts[0]);
        Boolean hasNewEpisode = Boolean.parseBoolean(parts[1]);

        sseEmitterService.sendBoolean(subscriberId, hasNewEpisode);
    }
}
