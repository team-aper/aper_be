package org.aper.web.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.aper.web.global.entity.BaseSoftDeleteEntity;

@Entity
@Getter
public class DeleteAccount extends BaseSoftDeleteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @OneToOne(cascade = CascadeType.MERGE, orphanRemoval = true)
    @JoinColumn
    private User user;

    public DeleteAccount(User user) {
        this.user = user;
    }

    public DeleteAccount() {
    }
}
