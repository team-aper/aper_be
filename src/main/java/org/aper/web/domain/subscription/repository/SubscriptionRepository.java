package org.aper.web.domain.subscription.repository;

import org.aper.web.domain.subscription.entity.Subscription;
import org.aper.web.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findAllByAuthor_UserId(Long authorId);

    boolean existsBySubscriberAndAuthor(User subscriber, User author);

    @Query("SELECT COUNT(s) FROM Subscription s WHERE s.author.userId = :authorId")
    Long countByAuthorId(Long authorId);


    List<Subscription> findAllBySubscriber_UserId(Long userId);
}
