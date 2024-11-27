package com.aperlibrary.subscription.entity;

import com.aperlibrary.global.entity.BaseEntity;
import com.aperlibrary.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
@Getter
@NoArgsConstructor
public class Subscription extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "subscriber_id", referencedColumnName = "userId")
    private User subscriber;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "userId")
    private User author;

    @Builder
    public Subscription(User subscriber, User author) {
        this.subscriber = subscriber;
        this.author = author;
        this.createdAt = LocalDateTime.now();
    }
}
