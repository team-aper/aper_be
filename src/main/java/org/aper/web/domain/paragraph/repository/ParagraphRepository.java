package org.aper.web.domain.paragraph.repository;

import org.aper.web.domain.paragraph.entity.Paragraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParagraphRepository extends JpaRepository<Paragraph, String> {

    Optional<Paragraph> findByUuid(String uuid);

    boolean existsByEpisodeId(Long episodeId);
}
