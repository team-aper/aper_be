package org.aper.web.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aper.web.domain.user.entity.User;

@NoArgsConstructor
@Getter
public class GetUsersForMainResponseDto {
    private Long userId;
    private String penName;
    private String fieldImage;
    private String description;

    @Builder
    public GetUsersForMainResponseDto(User user) {
        this.userId = user.getUserId();
        this.penName = user.getPenName();
        this.fieldImage = user.getFieldImage();
        this.description = user.getDescription();
    }

    public static GetUsersForMainResponseDto of(User user) {
        return GetUsersForMainResponseDto.builder().user(user).build();
    }
}
