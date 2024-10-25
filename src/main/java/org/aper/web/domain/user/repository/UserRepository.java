package org.aper.web.domain.user.repository;

import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.aper.web.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.isExposed = true")
    Page<User> findAllForMain(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.deleteAccount IS NULL")
    Optional<User> findByEmailWithOutDeleteAccount(String email);

    @Query("SELECT u FROM User u WHERE u.penName LIKE %:penName%")
    Page<User> findAllByPenNameContaining(Pageable pageable, String penName);

    @Query("SELECT u.userId FROM User u")
    List<Long> findAllUserId();

    @Query("SELECT u FROM User u WHERE u.userId IN (:ids)")
    List<User> findByIdList(List<Long> ids);

//    @Query("SELECT u FROM User u " +
//            "LEFT JOIN FETCH u.storyList s " +
//            "LEFT JOIN FETCH u.reviewsReceived r " +
//            "LEFT JOIN FETCH u.subscribers s " +
//            "WHERE u.userId IN :ids")
//    List<User> findByIdListWithStories(List<Long> ids);

    @Query("SELECT u.penName, u.fieldImage, u.description, u.userId, u.storyList, COUNT(r), COUNT(s) " +
            "FROM User u " +
            "LEFT JOIN u.reviewsReceived r " +
            "LEFT JOIN u.subscribers s " +
            "WHERE u.userId IN :ids " +
            "GROUP BY u.userId")
    List<Object[]> findUserWithSubscriberAndReviewCounts(List<Long> ids);

    @Query("SELECT u FROM User u " +
            "JOIN u.storyList st " +
            "LEFT JOIN Subscription s ON u.userId = s.author.userId " +
            "WHERE st.genre = :genre AND u.userId != :currentUserId " +
            "GROUP BY u " +
            "ORDER BY CASE WHEN COUNT(s.subscriber) = 0 THEN RAND() ELSE COUNT(s.subscriber) END DESC")
    List<User> findTop4ByGenreOrderBySubscriberCountDesc(StoryGenreEnum genre, Long currentUserId);

    @Query("SELECT u FROM User u WHERE u.userId = :userId AND u.userId != :exceptUserId")
    Optional<User> findByIdExceptMe(Long userId, Long exceptUserId);
}