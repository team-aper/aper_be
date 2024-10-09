package org.aper.web.domain.user.repository;

import org.aper.web.domain.user.entity.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {
    List<UserHistory> findAllByUserUserId(Long userId);
}
