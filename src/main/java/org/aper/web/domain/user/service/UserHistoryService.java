package org.aper.web.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.user.repository.UserHistoryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserHistoryService {
    private final UserHistoryRepository userHistoryRepository;
    
}
