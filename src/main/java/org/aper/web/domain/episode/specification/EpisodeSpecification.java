package org.aper.web.domain.episode.specification;

import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.springframework.data.jpa.domain.Specification;

public interface EpisodeSpecification {
    public static Specification<Episode> hasStoryGenre(StoryGenreEnum storyGenre) {
        return (root, query, criteriaBuilder) -> {
            if (storyGenre == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("story").get("genre"), storyGenre);
        };
    }

    public static Specification<Episode> isOnDisplay() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("onDisplay"));
    }

    public static Specification<Episode> isStoryOnDisplay() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("story").get("onDisplay"));
    }
}
