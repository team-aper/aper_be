package org.aper.web.domain.user.repository;

import com.aperlibrary.story.entity.constant.StoryGenreEnum;
import com.aperlibrary.user.entity.User;
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

    @Query("SELECT DISTINCT u, " +
            "CASE WHEN EXISTS (SELECT s FROM Subscription s " +
            "                 WHERE s.author.userId = u.userId " +
            "                 AND s.subscriber.userId = :userId) " +
            "     THEN true ELSE false END AS isAuthor, " +
            "  (SELECT COUNT(r) FROM Review r WHERE r.reviewee.userId = u.userId) AS reviewCount, " +
            "  (SELECT COUNT(s) FROM Subscription s WHERE s.subscriber.userId = u.userId) AS subscriberCount " +
            "FROM User u " +
            "LEFT JOIN FETCH u.storyList sl " +
            "WHERE u.userId IN :ids")
    List<Object[]> findUserWithSubscriberAndReviewCounts(List<Long> ids, Long userId);

    @Query("SELECT " +
            " (SELECT COUNT(r) FROM Review r WHERE r.reviewee.userId = :authorId) AS reviewCount, " +
            " (SELECT COUNT(cp) FROM ChatParticipant cp WHERE cp.user.userId = :authorId AND cp.isTutor = true) AS tutorChatCount " +
            "FROM User u " +
            "WHERE u.userId = :authorId")
    List<Object[]> findUserIsTutorAndReviewers(Long authorId);

    @Query("SELECT u FROM User u " +
            "JOIN u.storyList st " +
            "LEFT JOIN Subscription s ON u.userId = s.author.userId " +
            "WHERE st.genre = :genre AND u.userId != :currentUserId " +
            "GROUP BY u " +
            "ORDER BY CASE WHEN COUNT(s.subscriber) = 0 THEN RAND() ELSE COUNT(s.subscriber) END DESC")
    List<User> findTop4ByGenreOrderBySubscriberCountDesc(StoryGenreEnum genre, Long currentUserId);

    @Query("SELECT u FROM User u WHERE u.userId = :userId AND u.userId != :exceptUserId")
    Optional<User> findByIdExceptMe(Long userId, Long exceptUserId);

    @Query("SELECT u FROM User u " +
            "JOIN FETCH u.userHistories h " +
            "WHERE u.userId = :id")
    Optional<User> findByIdWithHistory(Long id);


    @Query("SELECT DISTINCT u, " +
            "CASE WHEN EXISTS (SELECT s FROM Subscription s " +
            "                 WHERE s.author.userId = u.userId) " +
            "     THEN true ELSE false END AS isAuthor, " +
            "  (SELECT COUNT(r) FROM Review r WHERE r.reviewee.userId = u.userId) AS reviewCount, " +
            "  (SELECT COUNT(s) FROM Subscription s WHERE s.subscriber.userId = u.userId) AS subscriberCount " +
            "FROM User u " +
            "LEFT JOIN FETCH u.storyList sl " +
            "WHERE u.requestTutor = true")
    List<Object[]> findByRequestTutorIsTrue();
}