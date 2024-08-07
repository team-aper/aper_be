package org.aper.web.domain.field.dto;

import lombok.Getter;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Getter
public class HomeResponseDto {
    private Boolean isMyField;
    private List<HomeDetailsResponseDto> detailsList;

    public HomeResponseDto(Boolean isMyField, List<HomeDetailsResponseDto> detailsList) {
        this.isMyField = isMyField;
        this.detailsList = detailsList;
    }
}
