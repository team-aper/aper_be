package org.aper.web.domain.user.repository;

import org.aper.web.domain.user.entity.DeleteAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface DeleteAccountRepository extends JpaRepository<DeleteAccount, Long> {
    @Query("SELECT da FROM DeleteAccount da WHERE da.createdAt <= :period")
    List<DeleteAccount> findAllToDelete(LocalDateTime period);

    @Query("DELETE FROM DeleteAccount da WHERE da.createdAt <= :period")
    void deleteAllByCreatedAtBefore(LocalDateTime period);
}
