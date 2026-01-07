package org.aper.web.global.event;

import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.elasticsearch.service.ElasticSyncService;
import org.aper.web.domain.user.service.DeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j(topic = "scheduling")
@Component
public class Scheduler {
    private final DeleteService deleteService;
    @Autowired(required = false)
    private ElasticSyncService syncService;

    public Scheduler(DeleteService deleteService) {
        this.deleteService = deleteService;
    }

    @Scheduled(cron = "0 0 6 * * *")//오전 6시 주기
    @Retryable(backoff = @Backoff(delay = 600000))//10분 주기로 3번까지 시도
    public void deleteAccountPerDay() {
        deleteService.deleteAccountScheduler();
    }

    @Scheduled(cron = "0 0 3 * * *")//오전 3시 주기
    @Retryable(backoff = @Backoff(delay = 600000))//10분 주기로 3번까지 시도
    public void syncMysqlElasticSearchUser() {
        if (syncService != null) {
            syncService.syncUser();
        }
    }

    @Scheduled(cron = "0 0 4 * * *")//오전 4시 주기
    @Retryable(backoff = @Backoff(delay = 600000))//10분 주기로 3번까지 시도
    public void syncMysqlElasticSearchEpisodes() {
        if (syncService != null) {
            syncService.syncEpisodes();
        }
    }
}
