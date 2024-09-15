package org.aper.web.domain.elasticsearch.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.elasticsearch.entity.document.ElasticSearchEpisodeDocument;
import org.aper.web.domain.elasticsearch.entity.document.ElasticSearchUserDocument;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ElasticSearchMapper {

    public List<ElasticSearchEpisodeDocument> HitsToEpisodeDocument(SearchHits<ElasticSearchEpisodeDocument> searchHits) {
        return searchHits.getSearchHits()
                .stream()
                .map(this::mapHitToDocument)
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

    private ElasticSearchEpisodeDocument mapHitToDocument(SearchHit<ElasticSearchEpisodeDocument> hit) {
        ElasticSearchEpisodeDocument document = hit.getContent();
        Map<String, List<String>> highlightMap = new HashMap<>(hit.getHighlightFields());
        String description = getHighlightDescriptionValue(highlightMap);
        document.setEpisodeDescription(description);
        return document;
    }

    private String getHighlightDescriptionValue(Map<String, List<String>> highlightMap) {
        List<String> highlightValues = highlightMap.get("episodeDescription");
        return (highlightValues != null && !highlightValues.isEmpty()) ? highlightValues.get(0) : null;
    }
}
