package org.aper.web.domain.subscription.repository;

import org.aper.web.domain.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findAllBySubscriber_UserId(Long subscriberId);

    List<Subscription> findAllByAuthor_UserId(Long authorId);
}
