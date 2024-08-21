package org.aper.web.domain.user.repository;

import org.aper.web.domain.user.entity.DeleteAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeleteAccountRepository extends JpaRepository<Long, DeleteAccount> {
}
