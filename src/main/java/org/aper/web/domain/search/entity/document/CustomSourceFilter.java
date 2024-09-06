package org.aper.web.domain.search.entity.document;

import org.springframework.data.elasticsearch.core.query.SourceFilter;

public class CustomSourceFilter implements SourceFilter {
    private final String[] excludes;

    public CustomSourceFilter(String[] excludes) {
        this.excludes = excludes;
    }

    @Override
    public String[] getIncludes() {
        // 포함할 필드를 설정하지 않으므로 null을 반환
        return null;
    }

    @Override
    public String[] getExcludes() {
        return excludes;
    }
}
