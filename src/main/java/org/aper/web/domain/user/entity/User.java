package org.aper.web.domain.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.aper.web.domain.chat.entity.ChatParticipant;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String fieldImage;

    @Column
    private String description;

    @Column
    private String penName;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column
    private Long point;

    @Column(name = "is_exposed", columnDefinition = "boolean default false")
    private boolean isExposed;

    @OneToMany(mappedBy = "user")
    private List<ChatParticipant> chatParticipants = new ArrayList<>();

    @OneToOne
    @JoinColumn
    private DeleteAccount deleteAccount;

    public User() {
    }

    @Builder
    public User(String penName, String password, String email, UserRoleEnum role) {
        this.penName = penName;
        this.password = password;
        this.email = email;
        this.role = role;
        this.point = 0L;
    }
    public void updatePoint(Long point) {
        this.point += point;
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void updatePenName(String penName) {
        this.penName = penName;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateFieldImage(String fieldImage) {
        this.fieldImage = fieldImage;
    }

    public void updateDeleteAccount(DeleteAccount deleteAccount) {
        this.deleteAccount = deleteAccount;
    }
}