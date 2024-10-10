package org.aper.web.domain.subscription.controller;

import org.aper.web.domain.subscription.service.SubscriptionService;
import org.aper.web.domain.user.entity.User;
import org.aper.web.global.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/user/{userId}")
    public ResponseDto<List<User>> getSubscribedAuthors(@PathVariable Long userId) {
        List<User> subscribedAuthors = subscriptionService.getSubscribedAuthors(userId);
        return ResponseDto.success("Author data", subscribedAuthors);
    }
}
