package org.aper.web.domain.paragraph.repository;

import org.aper.web.domain.paragraph.entity.Paragraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ParagraphRepository extends JpaRepository<Paragraph, String> {

    @Query("SELECT p FROM Paragraph p WHERE p.uuid = :uuid")
    Optional<Paragraph> findByUuid(String uuid);

}
