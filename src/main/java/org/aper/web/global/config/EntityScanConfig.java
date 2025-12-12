package org.aper.web.global.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = {
    "org.aper.web.domain.common.entity",
    "org.aper.web.domain.user.entity",
    "org.aper.web.domain.story.entity",
    "org.aper.web.domain.episode.entity",
    "org.aper.web.domain.paragraph.entity",
    "org.aper.web.domain.review.entity",
    "org.aper.web.domain.subscription.entity",
    "org.aper.web.domain.payment.entity",
    "org.aper.web.domain.curation.entity"
})
public class EntityScanConfig {
}
