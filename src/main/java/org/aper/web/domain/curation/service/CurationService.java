package org.aper.web.domain.curation.service;

import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.curation.repository.CurationRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CurationService {

    private final CurationRepository curationRepository;

    public CurationService(CurationRepository curationRepository) {
        this.curationRepository = curationRepository;
    }

}
