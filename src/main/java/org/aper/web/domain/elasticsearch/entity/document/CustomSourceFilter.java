package org.aper.web.domain.elasticsearch.entity.document;

import org.springframework.data.elasticsearch.core.query.SourceFilter;

public class CustomSourceFilter implements SourceFilter {
    private final String[] excludes;

    public CustomSourceFilter(String[] excludes) {
        this.excludes = excludes;
    }

    @Override
    public String[] getIncludes() {
        return null;
    }

    @Override
    public String[] getExcludes() {
        return excludes;
    }
}
