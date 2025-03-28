package org.aper.web.domain.story.repository;

import com.aperlibrary.story.entity.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long>, JpaSpecificationExecutor<Story> {

    @Query("SELECT s from Story s " +
            "JOIN s.user u " +
            "WHERE u.userId = :authorId"
    )
    Page<Story> findAllByStoriesWithPageAble(Long authorId, Pageable pageable);

    @Query("SELECT s from Story s " +
            "JOIN s.user u " +
            "WHERE u.userId = :authorId AND s.onDisplay = true"
    )
    Page<Story> findAllByStoriesOnlyPublishedWithPageAble(Long authorId, Pageable pageable);

    @Query("SELECT DISTINCT s FROM Story s " +
            "LEFT JOIN FETCH s.episodeList e " +
            "JOIN s.user u " +
            "WHERE s.id = :storyId")
    Optional<Story> findByStoryAuthor(Long storyId);

    boolean existsByIdAndUser_UserId(Long storyId, Long userId);
}
