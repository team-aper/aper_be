package org.aper.web.domain.paragraph.repository;

import com.aperlibrary.paragraph.entity.Paragraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParagraphRepository extends JpaRepository<Paragraph, String> {

    @Query("SELECT p FROM Paragraph p WHERE p.uuid = :uuid")
    Optional<Paragraph> findByUuid(String uuid);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Paragraph p WHERE p.episode.id IN :episodeIds")
    void deleteByEpisodeIds(@Param("episodeIds") List<Long> episodeIds);

}
