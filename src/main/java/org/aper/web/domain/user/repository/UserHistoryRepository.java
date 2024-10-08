package org.aper.web.domain.user.repository;

import org.aper.web.domain.user.entity.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {

}
