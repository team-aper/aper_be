package org.aper.web.global.batch.service;

import org.aper.web.global.batch.dto.BatchRequestDto.BatchRequest;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.transaction.annotation.Transactional;

public interface BatchService<T> {
    void processBatch(BatchRequest<T> request, UserDetailsImpl userDetails);
}

