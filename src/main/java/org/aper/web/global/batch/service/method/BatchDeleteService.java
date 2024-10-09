package org.aper.web.global.batch.service.method;

import java.util.List;
import java.util.Set;

public interface BatchDeleteService<T> {
    void handleDeletedOperation(List<T> itemPayloads, Set<String> deletedUuids, Long domainId);
}
