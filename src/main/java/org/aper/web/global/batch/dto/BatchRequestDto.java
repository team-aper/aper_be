package org.aper.web.global.batch.dto;

import java.util.List;

public class BatchRequestDto {

    public record BatchRequest<T> (
            List<BatchOperation<T>> batch
    ) {}

    public record BatchOperation<T> (
            String method,
            String url,
            List<T> body
    ) {}
}
