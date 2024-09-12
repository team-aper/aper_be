package org.aper.web.global.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.elasticsearch.service.ElasticSyncService;
import org.aper.web.domain.user.service.DeleteService;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DeleteAccountScheduler {
    private final DeleteService deleteService;
    private final ElasticSyncService syncService;

    @Scheduled(cron = "0 0 6 * * *")//오전 6시 주기
    @Retryable(backoff = @Backoff(delay = 600000))
    public void deleteAccountPerDay() {
        deleteService.deleteAccountScheduler();
    }

    @Scheduled(cron = "0 0 4 * * *")//오전 6시 주기
    @Retryable(backoff = @Backoff(delay = 600000))
    public void syncMysqlElasticSearch() {
        syncService.syncUser();
        syncService.syncEpisodes();
    }
}
