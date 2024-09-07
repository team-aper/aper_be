package org.aper.web.domain.search.repository;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import lombok.RequiredArgsConstructor;
import org.aper.web.domain.search.entity.document.CustomSourceFilter;
import org.aper.web.domain.search.entity.document.ElasticSearchEpisodeDocument;
import org.aper.web.domain.search.service.SearchMapper;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightFieldParameters;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightParameters;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CustomElasticSearchRepository {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    private final SearchMapper searchMapper;

    public List<ElasticSearchEpisodeDocument> searchWithQueryBuilders(String filter, StoryGenreEnum genre, Pageable pageable) {
        Query query = QueryBuilders.bool(bool -> {
                    bool.must(QueryBuilders.multiMatch(multi -> multi
                            .query(filter)
                            .fields("episodeTitle", "episodeDescription", "storyTitle")
                    ));
                    if (genre != null) {
                        bool.must(QueryBuilders.match(match -> match
                                .field("storyGenre")
                                .query(genre.name())
                        ));
                    }
                    bool.must(QueryBuilders.match(multi -> multi
                            .query("true")
                            .field("episodeOnDisplay")
                    ));
                    bool.must(QueryBuilders.match(multi -> multi
                            .query("true")
                            .field("storyOnDisplay")
                    ));
                    return bool;
                }
        );

        HighlightField episodeDescriptionField = new HighlightField("episodeDescription",
                HighlightFieldParameters.builder()
                        .withFragmentSize(80)
                        .withNoMatchSize(80)
                        .build());

        Highlight highlight = new Highlight(
                HighlightParameters.builder().build(),
                List.of(episodeDescriptionField)
        );

        HighlightQuery highlightQuery = new HighlightQuery(highlight, ElasticSearchEpisodeDocument.class);

        SourceFilter sourceFilter = new CustomSourceFilter(
                new String[] {"episodeDescription"}
        );

        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(query)
                .withHighlightQuery(highlightQuery)  // 하이라이트 쿼리 추가
                .withPageable(pageable)
                .withSourceFilter(sourceFilter)
                .build();

        SearchHits<ElasticSearchEpisodeDocument> searchHits = elasticsearchTemplate.search(searchQuery, ElasticSearchEpisodeDocument.class);

        return searchHits.getSearchHits()
                .stream()
                .map(searchMapper::mapHitToDocument)
                .toList();
    }

    public void delete(Long episodeId) {
        try {
            Query query = QueryBuilders.term(t -> t
                    .field("episodeId")
                    .value(episodeId)
            );
            NativeQuery searchQuery = NativeQuery.builder()
                    .withQuery(query)
                    .build();
            SearchHits<ElasticSearchEpisodeDocument> searchHits = elasticsearchTemplate.search(searchQuery, ElasticSearchEpisodeDocument.class);
            searchHits.forEach(hit -> {
                elasticsearchTemplate.delete(hit.getId(), ElasticSearchEpisodeDocument.class);
            });
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.DOCUMENT_DELETE_FAILED);
        }
    }

    public void update(Map<String, Object> data) {
        try {
            Long episodeId = Long.parseLong(data.get("episodeId").toString());
            Query query = QueryBuilders.term(t -> t
                    .field("episodeId")
                    .value(episodeId)
            );
            NativeQuery searchQuery = NativeQuery.builder()
                    .withQuery(query)
                    .build();
            SearchHits<ElasticSearchEpisodeDocument> searchHits = elasticsearchTemplate.search(searchQuery, ElasticSearchEpisodeDocument.class);
            searchHits.forEach(hit -> {
                String documentId = hit.getId();
                elasticsearchTemplate.delete(documentId, ElasticSearchEpisodeDocument.class);
            });
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.DOCUMENT_UPDATE_FAILED);
        }
    }

}
