package org.example.springaper.domain.user.repository;

import org.example.springaper.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); // 이걸 쿼리 메서드 라고 하는구나. DB랑 왔다갔다하니까

    Optional<User> findByEmail(String email);
}