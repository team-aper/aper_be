package org.aper.web.domain.elasticsearch.repository;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.search.FieldCollapse;
import com.aperlibrary.story.entity.constant.StoryGenreEnum;
import org.aper.web.domain.elasticsearch.entity.document.CustomSourceFilter;
import org.aper.web.domain.elasticsearch.entity.document.ElasticSearchEpisodeDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightFieldParameters;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightParameters;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElasticSearchQuery {
    public Query EpisodesAndStoriesQuery(String filter, StoryGenreEnum genre) {
        return QueryBuilders.bool(bool -> {
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
        });
    }

    public Query penNameQuery(String filter) {
        return QueryBuilders.bool(bool ->
                bool.must(QueryBuilders.matchPhrase(multi -> multi
                        .query(filter)
                        .field("penName")
                ))
        );
    }

    public Query episodeIdQuery(Long filter) {
        return QueryBuilders.term(t -> t
                .field("episodeId")
                .value(filter)
        );
    }

    public Query storyIdQuery(Long filter) {
        return QueryBuilders.term(t -> t
                .field("storyId")
                .value(filter)
        );
    }

    public Query userIdQuery(Long filter) {
        return QueryBuilders.term(t -> t
                .field("userId")
                .value(filter)
        );
    }

    public Query matchAllQuery() {
        return QueryBuilders.matchAll().build()._toQuery();
    }

    public NativeQuery episodeNativeQuery(Query query, HighlightQuery highlightQuery, Pageable pageable) {
        SourceFilter sourceFilter = new CustomSourceFilter(
                new String[] {"episodeDescription"}
        );

        FieldCollapse fieldCollapse = FieldCollapse.of(builder ->
            builder.field("episodeId")
        );

        return NativeQuery.builder()
                .withQuery(query)
                .withHighlightQuery(highlightQuery)  // 하이라이트 쿼리 추가
                .withPageable(pageable)
                .withSourceFilter(sourceFilter)
                .withFieldCollapse(fieldCollapse)
                .build();
    }

    public NativeQuery penNameNativeQuery(Query query, Pageable pageable) {
        return NativeQuery.builder()
                .withQuery(query)
                .withPageable(pageable)
                .build();
    }

    public NativeQuery onlyNativeQuery(Query query) {
        return NativeQuery.builder()
                .withQuery(query)
                .build();
    }

    public HighlightField episodeHighLightField(String descriptionField) {
        return  new HighlightField(descriptionField,
                HighlightFieldParameters.builder()
                        .withFragmentSize(80)
                        .withNoMatchSize(80)
                        .withNumberOfFragments(1)
                        .build());
    }

    public Highlight episodeHighLight(HighlightField highlightField) {
        return new Highlight(
                HighlightParameters.builder().build(),
                List.of(highlightField)
        );
    }

    public HighlightQuery episodeHighLightQuery(Highlight highlight) {
        return new HighlightQuery(highlight, ElasticSearchEpisodeDocument.class);
    }
}
