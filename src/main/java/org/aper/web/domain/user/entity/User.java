package org.aper.web.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aper.web.domain.common.constant.UserRoleEnum;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.domain.review.entity.Review;
import org.aper.web.domain.subscription.entity.Subscription;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id  // ⭐ NO @GeneratedValue! Synced from auth-server
    @Column(name = "user_id")
    private Long userId;

    // Basic info (synced from auth-server)
    @Schema(description = "Email (synced from auth-server)")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Schema(description = "Pen name (synced from auth-server)")
    @Column(nullable = false, length = 50)
    private String penName;

    @Schema(description = "Role (synced from auth-server)")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRoleEnum role;

    // Password field for UserDetailsImpl compatibility (NOT stored in aper_be DB)
    @Transient
    private String password;

    // Business fields (aper_be only)
    @Schema(description = "Profile image URL")
    private String fieldImage;

    @Schema(description = "User description/bio")
    @Column(columnDefinition = "TEXT")
    private String description;

    @Schema(description = "Contact email")
    @Column(name = "contact_email", length = 100)
    private String contactEmail;

    @Schema(description = "User points")
    @Column(nullable = false)
    private Long point = 0L;

    @Schema(description = "Request tutor status")
    private Boolean requestTutor = false;

    @Schema(description = "Is profile exposed publicly")
    private Boolean isExposed = false;

    @Schema(description = "Class description for tutoring")
    @Column(columnDefinition = "TEXT")
    private String classDescription;

    // Soft delete
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deletedAt;

    // Audit fields
    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Story> storyList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserHistory> userHistories = new ArrayList<>();

    @OneToMany(mappedBy = "reviewer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviewsWritten = new ArrayList<>();

    @OneToMany(mappedBy = "reviewee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviewsReceived = new ArrayList<>();

    @OneToMany(mappedBy = "subscriber", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Subscription> subscribers = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Subscription> subscribedTo = new ArrayList<>();

    @Builder
    public User(Long userId, String email, String penName, UserRoleEnum role, String fieldImage,
                String description, String contactEmail, Long point, Boolean requestTutor,
                Boolean isExposed, String classDescription) {
        this.userId = userId;
        this.email = email;
        this.penName = penName;
        this.role = role;
        this.fieldImage = fieldImage;
        this.description = description;
        this.contactEmail = contactEmail;
        this.point = point != null ? point : 0L;
        this.requestTutor = requestTutor != null ? requestTutor : false;
        this.isExposed = isExposed != null ? isExposed : false;
        this.classDescription = classDescription;
    }

    // Factory method for event-driven sync from auth-server
    public static User fromAuthServer(Long userId, String email, String penName, UserRoleEnum role) {
        return User.builder()
                .userId(userId)
                .email(email)
                .penName(penName)
                .role(role)
                .build();
    }

    // Business methods
    public void updateFieldImage(String fieldImage) {
        this.fieldImage = fieldImage;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public void addPoint(Long point) {
        this.point += point;
    }

    public void deductPoint(Long point) {
        this.point -= point;
    }

    public void updatePoint(Long point) {
        if (point >= 0) {
            addPoint(point);
        } else {
            deductPoint(Math.abs(point));
        }
    }

    public void updateRequestTutor(Boolean requestTutor) {
        this.requestTutor = requestTutor;
    }

    public void updateIsExposed(Boolean isExposed) {
        this.isExposed = isExposed;
    }

    public void updateClassDescription(String classDescription) {
        this.classDescription = classDescription;
    }

    public void updatePenName(String penName) {
        this.penName = penName;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateDeleteAccount(DeleteAccount deleteAccount) {
        // DeleteAccount relationship is managed via DeleteAccount entity
        // This method exists for backward compatibility
    }

    public void setRequestTutor(Boolean requestTutor) {
        this.requestTutor = requestTutor;
    }

    public void isExposed() {
        this.isExposed = true;
    }

    // Soft delete
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}
