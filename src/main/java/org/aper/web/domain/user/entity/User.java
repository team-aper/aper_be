package org.aper.web.domain.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.aper.web.domain.chat.entity.ChatParticipant;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.domain.subscription.entity.Subscription;
import org.aper.web.domain.user.entity.constant.UserRoleEnum;

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

    @Column
    private String contactEmail;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column
    private Long point;

    @Column(name = "is_exposed", columnDefinition = "boolean default false")
    private boolean isExposed;

    @Column
    private String classDescription;

    @OneToMany(mappedBy = "user")
    private List<ChatParticipant> chatParticipants = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id")
    private DeleteAccount deleteAccount;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Story> storyList;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private List<UserHistory> userHistories;

    @OneToMany(mappedBy = "reviewer", cascade = {CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Review> reviewsWritten;

    @OneToMany(mappedBy = "reviewee", cascade = {CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Review> reviewsReceived;

    @OneToMany(mappedBy = "subscriber", cascade = {CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Subscription> subscribers;

    public User() {
    }

    @Builder
    public User(String penName, String password, String email, UserRoleEnum role) {
        this.penName = penName;
        this.password = password;
        this.email = email;
        this.role = role;
        this.description = "안녕하세요, " + penName + "입니다.";
        this.fieldImage = "https://aper-image-bucket.s3.ap-northeast-2.amazonaws.com/fieldimage/craig-manners-BNgxioIWM0Y-unsplash.png";
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

    public void updateContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public void updateClassDescription(String classDescription) {
        this.classDescription = classDescription;
    }
}