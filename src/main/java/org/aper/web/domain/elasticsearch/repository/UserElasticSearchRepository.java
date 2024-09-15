package org.aper.web.domain.elasticsearch.repository;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.RequiredArgsConstructor;
import org.aper.web.domain.elasticsearch.entity.document.ElasticSearchUserDocument;
import org.aper.web.domain.elasticsearch.service.ElasticSearchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UserElasticSearchRepository {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    private final ElasticSearchMapper elasticSearchMapper;
    private final ElasticSearchQuery elasticSearchQuery;

    public List<ElasticSearchUserDocument> searchUsersWithQueryBuilders(String filter, Pageable pageable) {
        Query query = elasticSearchQuery.penNameQuery(filter);
        NativeQuery searchQuery = elasticSearchQuery.penNameNativeQuery(query, pageable);

        SearchHits<ElasticSearchUserDocument> searchHits = elasticsearchTemplate.search(searchQuery, ElasticSearchUserDocument.class);
        return elasticSearchMapper.HitsToUserDocument(searchHits);
    }

    public void delete(Long userId) {
        Query query = elasticSearchQuery.userIdQuery(userId);
        NativeQuery searchQuery = elasticSearchQuery.onlyNativeQuery(query);
        SearchHits<ElasticSearchUserDocument> searchHits = elasticsearchTemplate.search(searchQuery, ElasticSearchUserDocument.class);
        searchHits.forEach(hit -> {elasticsearchTemplate.delete(hit.getId(), ElasticSearchUserDocument.class);});
    }

    public void update(Map<String, Object> data) {
        Long userId = Long.parseLong(data.get("userId").toString());
        Query query = elasticSearchQuery.userIdQuery(userId);
        NativeQuery searchQuery = elasticSearchQuery.onlyNativeQuery(query);
        SearchHits<ElasticSearchUserDocument> searchHits = elasticsearchTemplate.search(searchQuery, ElasticSearchUserDocument.class);
        searchHits.forEach(hit -> {elasticsearchTemplate.delete(hit.getId(), ElasticSearchUserDocument.class);});
    }

    public List<Long> searchAllUserIdList() {
        Query query = elasticSearchQuery.matchAllQuery();
        NativeQuery searchQuery = elasticSearchQuery.onlyNativeQuery(query);

        SearchHits<ElasticSearchUserDocument> searchHits = elasticsearchTemplate.search(searchQuery, ElasticSearchUserDocument.class);
        return elasticSearchMapper.UserDocumentListToLong(elasticSearchMapper.HitsToUserDocument(searchHits));
    }
}
