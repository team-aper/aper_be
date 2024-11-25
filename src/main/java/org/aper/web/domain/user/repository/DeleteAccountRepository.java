package org.aper.web.domain.user.repository;

import com.aperlibrary.user.entity.DeleteAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface DeleteAccountRepository extends JpaRepository<DeleteAccount, Long> {
    @Query("SELECT da FROM DeleteAccount da WHERE da.createdAt <= :period")
    List<DeleteAccount> findAllToDelete(LocalDateTime period);

    @Modifying
    @Query("DELETE FROM DeleteAccount da WHERE da.createdAt <= :period")
    void deleteAllByCreatedAtBefore(LocalDateTime period);
}
