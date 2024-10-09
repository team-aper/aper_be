package org.aper.web.global.batch.service.method;

import java.util.List;
import java.util.Set;

public interface BatchPutService<T> {
    boolean handleModifiedOperation(List<T> itemPayloads, Set<String> deletedUuids);
}