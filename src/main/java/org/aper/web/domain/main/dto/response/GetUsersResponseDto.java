package org.aper.web.domain.main.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aper.web.domain.user.entity.User;

@NoArgsConstructor
@Getter
public class GetUsersResponseDto {
    private Long userId;
    private String penName;
    private String fieldImage;
    private String description;

    @Builder
    public GetUsersResponseDto(User user) {
        this.userId = user.getUserId();
        this.penName = user.getPenName();
        this.fieldImage = user.getFieldImage();
        this.description = user.getDescription();
    }

    public static GetUsersResponseDto of(User user) {
        return GetUsersResponseDto.builder().user(user).build();
    }
}
