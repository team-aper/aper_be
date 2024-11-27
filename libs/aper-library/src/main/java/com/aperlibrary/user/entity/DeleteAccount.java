package com.aperlibrary.user.entity;

import com.aperlibrary.global.entity.BaseSoftDeleteEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;


@Entity
@Getter
public class DeleteAccount extends BaseSoftDeleteEntity {

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public DeleteAccount(User user) {
        this.user = user;
    }

    public DeleteAccount() {
    }
}
