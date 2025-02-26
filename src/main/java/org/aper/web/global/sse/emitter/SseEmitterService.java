package org.aper.web.global.sse.emitter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SseEmitterService {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter createEmitter(Long userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(userId, emitter);

        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));

        return emitter;
    }

    public void sendBoolean(Long userId, Boolean hasNewEpisode) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                log.info("Sending to user {}: {}", userId, hasNewEpisode);
                emitter.send(SseEmitter.event().name("notification").data(hasNewEpisode));
            } catch (IOException e) {
                emitters.remove(userId);
            }
        }
    }
}
