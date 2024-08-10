package org.aper.web.domain.curation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.curation.service.CurationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/curation")
@RequiredArgsConstructor
@Slf4j(topic = "큐레이션 컨트롤러")
public class CurationController {
    private final CurationService curationService;
}
