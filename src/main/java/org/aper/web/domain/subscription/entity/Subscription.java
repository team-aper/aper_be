package org.aper.web.domain.subscription.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import org.aper.web.domain.common.entity.BaseEntity;
import org.aper.web.domain.user.entity.User;

@Entity
@Table(name = "subscriptions")
@Getter
@NoArgsConstructor
public class Subscription extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id", referencedColumnName = "user_id")
    private User subscriber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "user_id")
    private User author;

    @Builder
    public Subscription(User subscriber, User author) {
        this.subscriber = subscriber;
        this.author = author;
        this.createdAt = LocalDateTime.now();
    }
}
