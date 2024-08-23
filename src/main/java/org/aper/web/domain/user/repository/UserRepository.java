package org.aper.web.domain.user.repository;

import org.aper.web.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.isExposed = true")
    Page<User> findAllForMain(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.deleteAccount IS NULL")
    Optional<User> findByEmailWithOutDeleteAccount(String email);
}