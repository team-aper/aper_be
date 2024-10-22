package org.aper.web.global.sse.controller;

import org.aper.web.global.docs.NotificationControllerDocs;
import org.aper.web.global.sse.emitter.SseEmitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notifications")
public class NotificationController implements NotificationControllerDocs {

    private final SseEmitterService sseEmitterService;

    @Autowired
    public NotificationController(SseEmitterService sseEmitterService) {
        this.sseEmitterService = sseEmitterService;
    }

    @GetMapping("/subscribe/{userId}")
    public SseEmitter subscribe(@PathVariable Long userId) {
        return sseEmitterService.createEmitter(userId);
    }
}
