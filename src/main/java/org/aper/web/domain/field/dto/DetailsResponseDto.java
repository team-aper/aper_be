package org.aper.web.domain.field.dto;

import lombok.Getter;
import org.aper.web.domain.user.entity.User;

@Getter
public class DetailsResponseDto {
    private String penName;
    private String email;

    public DetailsResponseDto(User user) {
        this.penName = user.getPenName();
        this.email = user.getEmail();
    }
}
