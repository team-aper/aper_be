package org.aper.web.domain.search.repository;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import lombok.RequiredArgsConstructor;
import org.aper.web.domain.search.entity.document.CustomSourceFilter;
import org.aper.web.domain.search.entity.document.ElasticSearchEpisodeDocument;
import org.aper.web.domain.search.service.SearchMapper;
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

@Repository
@RequiredArgsConstructor
public class CustomElasticSearchRepository {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    private final SearchMapper searchMapper;

    public List<ElasticSearchEpisodeDocument> searchWithQueryBuilders(String filter, String genre, Pageable pageable) {
        Query query = QueryBuilders.bool(bool -> bool
                .must(QueryBuilders.multiMatch(multi -> multi
                        .query(filter)
                        .fields("episodeTitle", "episodeDescription", "storyTitle")
                ))
                .must(QueryBuilders.match(match -> match
                        .field("storyGenre")
                        .query(genre)
                ))
        );

        HighlightField episodeDescriptionField = new HighlightField("episodeDescription",
                HighlightFieldParameters.builder()
                        .withFragmentSize(100)   // 80글자 조각으로 자름
                        .withNoMatchSize(100)    // 일치하지 않으면 처음 80글자
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
}
