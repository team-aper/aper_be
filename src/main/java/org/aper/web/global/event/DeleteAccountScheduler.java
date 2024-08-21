package org.aper.web.global.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.user.service.DeleteService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DeleteAccountScheduler {
    private final DeleteService deleteService;

    @Scheduled(cron = "0 0 6 * * *")
    public void deleteAccountPerDay() {
        log.info("start delete account in DeleteAccountScheduler");
        deleteService.deleteAccountScheduler();
    }
}
