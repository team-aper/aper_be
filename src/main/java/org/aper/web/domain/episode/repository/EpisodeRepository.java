package org.aper.web.domain.episode.repository;

import org.aper.web.domain.episode.entity.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long>, JpaSpecificationExecutor<Episode> {
    @Query("SELECT e FROM Episode e " +
            "JOIN e.story s " +
            "JOIN s.user u " +
            "WHERE u.userId = :authorId")
    List<Episode> findAllByEpisode(Long authorId);

    @Query("SELECT e FROM Episode e " +
            "JOIN e.story s " +
            "JOIN s.user u " +
            "WHERE u.userId = :authorId AND e.onDisplay = true AND s.onDisplay = true"
    )
    List<Episode> findAllByEpisodeOnlyPublished(Long authorId);

    @Query("SELECT e FROM Episode e " +
            "JOIN e.story s " +
            "JOIN s.user u " +
            "WHERE e.id = :episodeId"
    )
    Optional<Episode> findByEpisodeAuthor(Long episodeId);

    List<Episode> findAllByStoryId(Long storyId);

}