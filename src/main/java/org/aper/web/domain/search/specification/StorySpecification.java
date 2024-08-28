package org.aper.web.domain.search.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.story.constant.StoryGenreEnum;
import org.aper.web.domain.story.entity.Story;
import org.springframework.data.jpa.domain.Specification;

public interface StorySpecification {

    public static Specification<Story> joinWithEpisodeAndApplyFilters(
            StoryGenreEnum genre,
            String storyTitle,
            String episodeTitle,
            String episodeParagraph,
            Boolean isEpisodeOnDisplay) {

        return (root, query, cb) -> {
            Join<Story, Episode> episodeJoin = root.join("episodeList");

            Predicate predicate = cb.conjunction();

            if (genre != null) {
                predicate = cb.and(predicate, cb.equal(root.get("genre"), genre));
            }

            if (storyTitle != null && !storyTitle.isEmpty()) {
                predicate = cb.and(predicate, cb.like(root.get("title"), "%" + storyTitle + "%"));
            }

            if (episodeTitle != null && !episodeTitle.isEmpty()) {
                predicate = cb.and(predicate, cb.like(episodeJoin.get("title"), "%" + episodeTitle + "%"));
            }

            if (episodeParagraph != null && !episodeParagraph.isEmpty()) {
                predicate = cb.and(predicate, cb.like(episodeJoin.get("description"), "%" + episodeParagraph + "%"));
            }

            if (Boolean.TRUE.equals(isEpisodeOnDisplay)) {
                predicate = cb.and(predicate, cb.isTrue(episodeJoin.get("onDisplay")));
            }

            return predicate;
        };
    }
}
