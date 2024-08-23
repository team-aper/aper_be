package org.aper.web.domain.story.repository;

import org.aper.web.domain.story.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {
    @Query("SELECT s from Story s " +
            "JOIN s.user u " +
            "WHERE u.userId = :authorId"
    )
    List<Story> findAllByStories(Long authorId);

    @Query("SELECT s from Story s " +
            "JOIN s.user u " +
            "WHERE u.userId = :authorId AND s.onDisplay = true"
    )
    List<Story> findAllByStoriesOnlyPublished(Long authorId);

    @Query("SELECT s FROM Story s " +
            "JOIN s.user u " +
            "WHERE s.id = :storyId"
    )
    Optional<Story> findByStoryAuthor(Long storyId);

    boolean existsByIdAndUser_UserId(Long storyId, Long userId);

}
