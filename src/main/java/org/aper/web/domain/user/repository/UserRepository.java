package org.aper.web.domain.user.repository;

import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
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

    @Query("SELECT u FROM User u WHERE u.id IN (:ids)")
    List<User> findByIdList(List<Long> ids);

    @Query(value = "SELECT * FROM ( " +
            "   SELECT s.*, ROW_NUMBER() OVER (PARTITION BY u.user_id ORDER BY s.public_date DESC) as rn " +
            "   FROM users u " +
            "   LEFT JOIN stories s ON u.user_id = s.user_id " +
            "   WHERE u.user_id IN :ids " +
            ") as subquery WHERE rn <= 3", nativeQuery = true)
    List<User> findByIdListWithStories(List<Long> ids);

}