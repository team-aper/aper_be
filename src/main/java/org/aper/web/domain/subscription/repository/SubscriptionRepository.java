package org.aper.web.domain.subscription.repository;

import org.aper.web.domain.subscription.entity.Subscription;
import org.aper.web.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Page<Subscription> findAllBySubscriber_UserId(Long userId, Pageable pageable);

    List<Subscription> findAllByAuthor_UserId(Long userId);

    boolean existsBySubscriberAndAuthor(User subscriber, User author);

    @Query("SELECT COUNT(s) FROM Subscription s WHERE s.author.userId = :authorId")
    Long countByAuthorId(Long authorId);

    Optional<Subscription> findBySubscriberAndAuthor(User subscriber, User author);

    Optional<Subscription> findByAuthorUserIdAndSubscriberUserId(Long authorId, Long subscriberId);

    boolean existsBySubscriber_UserId(Long userId);
}
