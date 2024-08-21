package org.aper.web.global.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.user.service.DeleteService;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@RequiredArgsConstructor
public class DeleteAccountScheduler {
    private final DeleteService deleteService;

    @Scheduled(fixedRate = 60000)
    public void deleteAccountPerDay() {
        log.info("계정 삭제 실시");
        deleteService.deleteAccountScheduler();
    }
}
