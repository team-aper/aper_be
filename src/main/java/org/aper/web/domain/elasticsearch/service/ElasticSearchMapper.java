package org.aper.web.domain.elasticsearch.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.elasticsearch.entity.document.ElasticSearchEpisodeDocument;
import org.aper.web.domain.elasticsearch.entity.document.ElasticSearchUserDocument;
import org.aper.web.domain.search.service.SearchMapper;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ElasticSearchMapper {
    private final SearchMapper searchMapper;

    public List<ElasticSearchEpisodeDocument> HitsToEpisodeDocument(SearchHits<ElasticSearchEpisodeDocument> searchHits) {
        return searchHits.getSearchHits()
                .stream()
                .map(searchMapper::mapHitToDocument)
                .toList();
    }

    public List<ElasticSearchUserDocument> HitsToUserDocument(SearchHits<ElasticSearchUserDocument> searchHits) {
        return searchHits.getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .toList();
    }

    public List<Long> UserDocumentListToLong(List<ElasticSearchUserDocument> userDocumentList) {
        return userDocumentList.stream()
                .map(ElasticSearchUserDocument::getUserId)
                .toList();
    }

    public List<Long> filterIdToAdd(Set<Long> mysqlIdsSet, Set<Long> esIdsSet) {
        return mysqlIdsSet.stream()
                .filter(id -> !esIdsSet.contains(id))
                .toList();
    }

    public List<Long> filterIdToDelete(Set<Long> esIdsSet, Set<Long> mysqlIdsSet) {
        return esIdsSet.stream()
                .filter(id -> !mysqlIdsSet.contains(id))
                .toList();
    }
}
