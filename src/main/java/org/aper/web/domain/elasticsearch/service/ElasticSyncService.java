package org.aper.web.domain.elasticsearch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.elasticsearch.repository.EpisodesElasticSearchRepository;
import org.aper.web.domain.elasticsearch.repository.UserElasticSearchRepository;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.kafka.service.KafkaEpisodesProducerService;
import org.aper.web.domain.kafka.service.KafkaUserProducerService;
import org.aper.web.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Sync ElasticSearch with MySQL DATA (TABLE : episodes, user)")
public class ElasticSyncService {
    private final EpisodeRepository episodeRepository;
    private final UserRepository userRepository;
    private final EpisodesElasticSearchRepository episodesSearchRepository;
    private final UserElasticSearchRepository userSearchRepository;
    private final KafkaEpisodesProducerService episodesProducerService;
    private final KafkaUserProducerService userProducerService;
    private final ElasticSearchMapper searchMapper;

    public void syncEpisodes() {
        List<Long> mysqlEpisodeIds = episodeRepository.findAllEpisodeId();
        List<Long> esEpisodeIds = episodesSearchRepository.searchAllEpisodeIdList();

        Set<Long> mysqlIdsSet = new HashSet<>(mysqlEpisodeIds);
        Set<Long> esIdsSet = new HashSet<>(esEpisodeIds);

        List<Long> idsToAdd = searchMapper.filterIdToAdd(mysqlIdsSet, esIdsSet);
        List<Long> idsToDelete = searchMapper.filterIdToDelete(esIdsSet, mysqlIdsSet);

        episodeRepository.findByIdList(idsToAdd)
                .forEach(episodesProducerService::sendCreate);

        idsToDelete.forEach(episodesProducerService::sendDelete);
    }

    public void syncUser() {
        List<Long> mysqlUserIds = userRepository.findAllUserId();
        List<Long> esUserIds = userSearchRepository.searchAllUserIdList();

        Set<Long> mysqlIdsSet = new HashSet<>(mysqlUserIds);
        Set<Long> esIdsSet = new HashSet<>(esUserIds);

        List<Long> idsToAdd = searchMapper.filterIdToAdd(mysqlIdsSet, esIdsSet);
        List<Long> idsToDelete = searchMapper.filterIdToDelete(esIdsSet, mysqlIdsSet);

        userRepository.findByIdList(idsToAdd)
                .forEach(userProducerService::sendCreate);
        idsToDelete.forEach(userProducerService::sendDelete);
    }
}
