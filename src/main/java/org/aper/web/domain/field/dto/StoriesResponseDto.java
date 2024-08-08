package org.aper.web.domain.field.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class StoriesResponseDto {
    private boolean isMyField;
    private List<StoriesDetailsResponseDto> storiesList;
    public StoriesResponseDto(boolean isMyField, List<StoriesDetailsResponseDto> storiesList) {
        this.isMyField = isMyField;
        this.storiesList = storiesList;
    }
}
