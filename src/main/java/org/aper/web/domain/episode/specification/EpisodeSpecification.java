package org.aper.web.domain.episode.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.story.entity.Story;
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

    public static Specification<Episode> joinWithEpisodeAndApplyFilters(
            StoryGenreEnum genre,
            String storyTitle,
            String episodeTitle,
            String episodeParagraph) {

        return (root, query, cb) -> {
            Join<Episode, Story> storyJoin = root.join("story");

            Predicate predicate = cb.conjunction();

            predicate = cb.and(predicate, cb.isTrue(root.get("onDisplay")));
            predicate = cb.and(predicate, cb.isTrue(storyJoin.get("onDisplay")));

            if (genre != null) {
                predicate = cb.and(predicate, cb.equal(root.get("genre"), genre));
            }

            if (storyTitle != null && !storyTitle.isEmpty()) {
                predicate = cb.or(predicate, cb.like(storyJoin.get("title"), "%" + storyTitle + "%"));
            }

            if (episodeTitle != null && !episodeTitle.isEmpty()) {
                predicate = cb.or(predicate, cb.like(root.get("title"), "%" + episodeTitle + "%"));
            }

            if (episodeParagraph != null && !episodeParagraph.isEmpty()) {
                predicate = cb.or(predicate, cb.like(root.get("description"), "%" + episodeParagraph + "%"));
            }

            return predicate;
        };
    }
}
