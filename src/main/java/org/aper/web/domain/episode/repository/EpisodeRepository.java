package org.aper.web.domain.episode.repository;

import org.aper.web.domain.episode.entity.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    @Query("SELECT ep FROM Episode ep" +
            "JOIN FETCH Story s" +
            "WHERE ep.story.id = s.id AND s.user.userId"
    )
    List<Episode> findAllByEpisode(Long authorId);

    @Query("SELECT ep FROM Episode ep" +
            "JOIN FETCH Story s" +
            "WHERE ep.story.id = s.id AND s.user.userId AND ep.onDisplay = true"
    )
    List<Episode> findAllByEpisodeOnlyPublished(Long authorId);
}
