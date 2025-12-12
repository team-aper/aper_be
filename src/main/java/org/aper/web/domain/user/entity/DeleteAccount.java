package org.aper.web.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aper.web.domain.common.entity.BaseSoftDeleteEntity;

@Entity
@Getter
@Table(name = "delete_account")
@NoArgsConstructor
public class DeleteAccount extends BaseSoftDeleteEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public DeleteAccount(User user) {
        this.user = user;
    }
}
