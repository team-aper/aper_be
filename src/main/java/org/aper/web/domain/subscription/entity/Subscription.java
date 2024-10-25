package org.aper.web.domain.subscription.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aper.web.domain.user.entity.User;

@Entity
@Table(name = "subscriptions")
@Getter
@NoArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    }
}
