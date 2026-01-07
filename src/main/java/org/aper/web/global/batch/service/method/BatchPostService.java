package org.aper.web.global.batch.service.method;

import org.aper.web.entity.episode.entity.Episode;

import java.util.List;

public interface BatchPostService<T> {
    boolean handleAddedOperation(List<T> itemPayloads, Episode domain);
}