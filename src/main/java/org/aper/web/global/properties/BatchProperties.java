package org.aper.web.global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.jpa.properties.hibernate.jdbc")
public class BatchProperties {
    private int batchSize;
}
