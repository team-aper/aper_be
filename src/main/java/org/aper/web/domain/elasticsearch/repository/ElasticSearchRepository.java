package org.aper.web.domain.elasticsearch.repository;

import org.aper.web.domain.elasticsearch.entity.document.ElasticSearchEpisodeDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

//복잡한 쿼리 구현은 여기서 불가능
public interface ElasticSearchRepository extends ElasticsearchRepository<ElasticSearchEpisodeDocument, Long> {
    @Query("{\"bool\": {\"must\": [{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"episodeTitle\", \"episodeDescription\", \"storyTitle\"]}}, {\"match\": {\"storyGenre\": \"?1\"}}]}}")
    Page<ElasticSearchEpisodeDocument> searchEpisode(String filter, String genre, Pageable pageable);
}
