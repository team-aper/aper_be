package org.aper.web.domain.episode.repository;

import com.aperlibrary.episode.entity.Episode;
import com.aperlibrary.story.entity.constant.StoryGenreEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long>, JpaSpecificationExecutor<Episode> {
    @Query("SELECT e FROM Episode e " +
            "JOIN e.story s " +
            "JOIN s.user u " +
            "WHERE u.userId = :authorId")
    List<Episode> findAllByUserId(Long authorId);

    @Query("SELECT e FROM Episode e LEFT JOIN FETCH e.paragraphs WHERE e.id = :id")
    Optional<Episode> findByIdWithParagraphs(@Param("id") Long id);

    @Query("SELECT e FROM Episode e " +
            "JOIN e.story s " +
            "WHERE s.id = :storyId AND e.onDisplay = true AND s.onDisplay = true")
    List<Episode> findAllByStoryOnlyPublished(@Param("storyId") Long storyId);

    @Query("SELECT e FROM Episode e " +
            "JOIN e.story s " +
            "JOIN s.user u " +
            "WHERE u.userId = :authorId")
    Page<Episode> findAllByUserIdWithPageAble(Long authorId, Pageable pageable);

    @Query("SELECT e FROM Episode e " +
            "JOIN e.story s " +
            "JOIN s.user u " +
            "WHERE u.userId = :authorId AND e.onDisplay = true AND s.onDisplay = true"
    )
    Page<Episode> findAllByEpisodeOnlyPublishedWithPageAble(Long authorId, Pageable pageable);

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

    @Query("SELECT e.id, " +
            "CASE " +
            "WHEN LOCATE(:filter, e.description) > 0 THEN " +
            "SUBSTRING(e.description, " +
            "GREATEST(1, LOCATE(:filter, e.description) - 40), " +
            "LEAST(80, LENGTH(e.description) - LOCATE(:filter, e.description) + LENGTH(:filter) + 40)) " +
            "ELSE " +
            "SUBSTRING(e.description, 1, 80) " +
            "END AS description, " +
            "s, " +
            "u " +
            "FROM Episode e " +
            "JOIN e.story s " +
            "JOIN s.user u " +
            "WHERE (s.title LIKE %:filter% " +
            "OR e.title LIKE %:filter% " +
            "OR e.description LIKE %:filter%) " +
            "AND (:genre IS NULL OR s.genre = :genre) " +
            "AND e.onDisplay = true AND s.onDisplay = true"
    )
    Page<Object[]> findAllByTitleAndDescription(Pageable pageable,
                                                @Param("genre") StoryGenreEnum genre,
                                                @Param("filter") String filter);

    @Query("SELECT e.id FROM Episode e")
    List<Long> findAllEpisodeId();

    @Query("SELECT e FROM Episode e WHERE e.id IN (:episodeIds)")
    List<Episode> findByIdList(List<Long> episodeIds);

    @Query("SELECT e FROM Episode e " +
            "JOIN e.story s " +
            "JOIN Subscription sub ON s.user.userId = sub.author.userId " +
            "WHERE sub.subscriber.userId = :subscriberId " +
            "AND e.onDisplay = true " +
            "AND e.publicDate > sub.createdAt " +
            "ORDER BY e.publicDate DESC")
    Page<Episode> findLatestEpisodesBySubscriberSinceSubscription(@Param("subscriberId") Long subscriberId, Pageable pageable);

    List<Episode> findByOnDisplayTrueAndStoryOnDisplayTrue();
}