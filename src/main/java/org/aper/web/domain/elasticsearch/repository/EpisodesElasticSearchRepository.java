package org.aper.web.domain.elasticsearch.repository;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.RequiredArgsConstructor;
import org.aper.web.domain.elasticsearch.entity.document.ElasticSearchEpisodeDocument;
import org.aper.web.domain.elasticsearch.entity.document.ElasticSearchUserDocument;
import org.aper.web.domain.elasticsearch.service.ElasticSearchMapper;
import org.aper.web.domain.search.service.SearchMapper;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class EpisodesElasticSearchRepository {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    private final SearchMapper searchMapper;
    private final ElasticSearchMapper elasticSearchMapper;
    private final ElasticSearchQuery elasticSearchQuery;

    public List<ElasticSearchEpisodeDocument> searchEpisodesWithQueryBuilders(String filter, StoryGenreEnum genre, Pageable pageable) {
        Query query = elasticSearchQuery.EpisodesAndStoriesQuery(filter, genre);
        HighlightField episodeDescriptionField = elasticSearchQuery.episodeHighLightField("episodeDescription");
        Highlight highlight = elasticSearchQuery.episodeHighLight(episodeDescriptionField);
        HighlightQuery highlightQuery = elasticSearchQuery.episodeHighLightQuery(highlight);
        NativeQuery searchQuery = elasticSearchQuery.episodeNativeQuery(query, highlightQuery, pageable);

        SearchHits<ElasticSearchEpisodeDocument> searchHits = elasticsearchTemplate.search(searchQuery, ElasticSearchEpisodeDocument.class);
        return elasticSearchMapper.HitsToEpisodeDocument(searchHits);
    }

    public void delete(Long episodeId) {
        Query query = elasticSearchQuery.episodeIdQuery(episodeId);
        NativeQuery searchQuery = elasticSearchQuery.onlyNativeQuery(query);
        SearchHits<ElasticSearchEpisodeDocument> searchHits = elasticsearchTemplate.search(searchQuery, ElasticSearchEpisodeDocument.class);
        searchHits.forEach(hit -> {elasticsearchTemplate.delete(hit.getId(), ElasticSearchEpisodeDocument.class);});
    }

    public void update(Map<String, Object> data) {
        Long episodeId = Long.parseLong(data.get("episodeId").toString());
        Query query = elasticSearchQuery.episodeIdQuery(episodeId);
        NativeQuery searchQuery = elasticSearchQuery.onlyNativeQuery(query);
        SearchHits<ElasticSearchEpisodeDocument> searchHits = elasticsearchTemplate.search(searchQuery, ElasticSearchEpisodeDocument.class);
        searchHits.forEach(hit -> {elasticsearchTemplate.delete(hit.getId(), ElasticSearchEpisodeDocument.class);});
    }

    public void updateOnlyStory(Map<String, Object> data) {
        Long storyId = Long.parseLong(data.get("storyId").toString());
        Query query = elasticSearchQuery.storyIdQuery(storyId);
        NativeQuery searchQuery = elasticSearchQuery.onlyNativeQuery(query);
        SearchHits<ElasticSearchEpisodeDocument> searchHits = elasticsearchTemplate.search(searchQuery, ElasticSearchEpisodeDocument.class);
        searchHits.forEach(hit -> {elasticsearchTemplate.delete(hit.getId(), ElasticSearchEpisodeDocument.class);});
    }

    public List<Long> searchAllEpisodeIdList() {
        Query query = elasticSearchQuery.matchAllQuery();
        NativeQuery searchQuery = elasticSearchQuery.onlyNativeQuery(query);

        SearchHits<ElasticSearchUserDocument> searchHits = elasticsearchTemplate.search(searchQuery, ElasticSearchUserDocument.class);
        return elasticSearchMapper.UserDocumentListToLong(elasticSearchMapper.HitsToUserDocument(searchHits));
    }
}
