package org.aper.web.domain.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

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
}