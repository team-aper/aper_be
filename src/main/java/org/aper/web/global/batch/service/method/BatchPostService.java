package org.aper.web.global.batch.service.method;

import org.aper.web.domain.episode.entity.Episode;

import java.util.List;

public interface BatchPostService<T> {
    boolean handleAddedOperation(List<T> itemPayloads, Episode domain);
}