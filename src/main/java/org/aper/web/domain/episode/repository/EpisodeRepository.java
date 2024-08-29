package org.aper.web.domain.episode.repository;

import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
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

     @Modifying(clearAutomatically = true, flushAutomatically = true)
     @Query("UPDATE Episode e SET e.chapter = e.chapter - 1 WHERE e.story.id = :storyId AND e.chapter > :chapter")
     void decrementChaptersAfterDeletion(@Param("storyId") Long storyId, @Param("chapter") Long chapter);

    List<Episode> findAllByStoryId(Long storyId);

    @Query("SELECT e.id, e.title, " +
            "CASE " +
            "WHEN LOCATE(:episodeParagraph, e.description) > 0 THEN " +
            "SUBSTRING(e.description, " +
            "GREATEST(1, LOCATE(:episodeParagraph, e.description) - 40), " +
            "LEAST(80, LENGTH(e.description) - LOCATE(:episodeParagraph, e.description) + LENGTH(:episodeParagraph) + 40)) " +
            "ELSE " +
            "SUBSTRING(e.description, 1, 80) " +
            "END AS snippet " +
            "FROM Episode e " +
            "JOIN FETCH e.story s " +
            "WHERE (s.title LIKE %:storyTitle% " +
            "OR e.title LIKE %:episodeTitle% " +
            "OR e.description LIKE %:episodeParagraph%) " +
            "AND (:genre IS NULL OR s.genre = :genre)")
    Page<Episode> findEpisodesWithSnippet(Pageable pageable,
                                           @Param("genre") StoryGenreEnum genre,
                                           @Param("storyTitle") String storyTitle,
                                           @Param("episodeTitle") String episodeTitle,
                                           @Param("episodeParagraph") String episodeParagraph);
}