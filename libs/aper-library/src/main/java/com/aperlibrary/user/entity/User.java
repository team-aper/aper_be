package com.aperlibrary.user.entity;

import com.aperlibrary.chat.entity.ChatParticipant;
import com.aperlibrary.review.entity.Review;
import com.aperlibrary.story.entity.Story;
import com.aperlibrary.subscription.entity.Subscription;
import com.aperlibrary.user.entity.constant.UserRoleEnum;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    @Column
    private Boolean requestTutor;

    @Column(name = "is_exposed", columnDefinition = "boolean default false")
    private Boolean isExposed;

    @Column
    private String classDescription;

    @OneToMany(mappedBy = "user")
    private List<ChatParticipant> chatParticipants = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
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
        Random random = new Random();
        this.email = email;
        this.penName = penName;
        this.password = password;
        this.role = role;
        this.description = "안녕하세요, " + penName + "입니다.";
        this.fieldImage = "/images/im" + random.nextInt(9) + 1 + ".jpg";
        this.point = 0L;
        this.requestTutor = Boolean.FALSE;
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

    public void isExposed() {
        this.isExposed = true;
    }

    public void setRequestTutor(Boolean aTrue) {this.requestTutor = aTrue; }
}