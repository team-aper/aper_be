package org.aper.web.domain.subscription.controller;

import org.aper.web.domain.subscription.dto.SubscriptionResponseDto.AuthorRecommendations;
import org.aper.web.domain.subscription.dto.SubscriptionResponseDto.IsSubscribed;
import org.aper.web.domain.subscription.dto.SubscriptionResponseDto.SubscribedAuthors;
import org.aper.web.domain.subscription.service.SubscriptionService;
import org.aper.web.global.docs.SubscriptionControllerDocs;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscribe")
public class SubscriptionController implements SubscriptionControllerDocs {

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/{authorId}")
    public ResponseDto<Void> subscribe(@PathVariable Long authorId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        subscriptionService.subscribe(userDetails, authorId);
        return ResponseDto.success("Subscribed successfully");
    }

    @GetMapping("/is-subscribed/{authorId}")
    public ResponseDto<IsSubscribed> isSubscribed(@PathVariable Long authorId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        IsSubscribed isSubscribed = subscriptionService.isSubscribed(userDetails, authorId);
        return ResponseDto.success("is Subscribed", isSubscribed);
    }

    @GetMapping("/subscribed-authors")
    public ResponseDto<SubscribedAuthors> getSubscribedAuthors(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        SubscribedAuthors subscribedAuthors = subscriptionService.getSubscribedAuthors(userDetails);
        return ResponseDto.success("Subscribed Authors Data", subscribedAuthors);
    }

    @GetMapping("/recommended-authors")
    public ResponseDto<AuthorRecommendations> getRecommendedAuthors(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        AuthorRecommendations recommendedAuthors = subscriptionService.getRecommendedAuthors(userDetails);
        return ResponseDto.success("Recommended Authors Data", recommendedAuthors);
    }

}
