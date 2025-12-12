package org.aper.web.domain.curation.repository;

import org.aper.web.domain.curation.entity.Curation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CurationRepository  extends JpaRepository<Curation, Long> {
    Page<Curation> findAll(Pageable pageable);

    @Query("SELECT c FROM Curation c JOIN FETCH c.episode e JOIN FETCH e.story s JOIN FETCH s.user u WHERE e.onDisplay = true AND s.onDisplay = true")
    Page<Curation> findAllForMain(Pageable pageable);
}
