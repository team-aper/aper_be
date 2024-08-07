package org.aper.web.domain.curation.service;

import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.curation.dto.response.GetCurationsResponseDto;
import org.aper.web.domain.curation.entity.Curation;
import org.aper.web.domain.curation.repository.CurationRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CurationService {

    private final CurationRepository curationRepository;

    public CurationService(CurationRepository curationRepository) {
        this.curationRepository = curationRepository;
    }

    public List<GetCurationsResponseDto> getCurations(int page, int size) {
        PageRequest pageRequest = PageRequest.of(Math.max(page - 1, 0), size, Sort.Direction.DESC, "createdAt");

        List<Curation> curations  = curationRepository.findAllForMain(pageRequest).getContent();
        List<GetCurationsResponseDto> responseDtoList = new ArrayList<>();

        for (Curation curation : curations) {
            responseDtoList.add(new GetCurationsResponseDto(curation));
        }

        return responseDtoList;
    }
}
