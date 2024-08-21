package org.aper.web.domain.story.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aper.web.domain.story.constant.StoryGenreEnum;
import org.aper.web.domain.story.constant.StoryLineStyleEnum;
import org.aper.web.domain.story.constant.StoryRoutineEnum;
import org.aper.web.domain.user.entity.User;
import org.aper.web.global.entity.BaseSoftDeleteEntity;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "stories")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Story extends BaseSoftDeleteEntity {

    @Id
    @GeneratedValue
    private Long storyId;

    @NotBlank(message = "field title is blank")
    @Schema(description = "Post Title", nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StoryRoutineEnum routine;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StoryGenreEnum genre;

    @Column(name = "line_style", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StoryLineStyleEnum lineStyle;

    @Column(name = "on_display", columnDefinition = "boolean default false")
    private boolean onDisplay;

    @Column(nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime publicDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Story(String title, StoryRoutineEnum routine, StoryGenreEnum genre, StoryLineStyleEnum lineStyle, User user) {
        this.title = title;
        this.routine = routine;
        this.genre = genre;
        this.lineStyle = lineStyle;
        this.user = user;
    }

    public void updateOnDisplay() {
        this.onDisplay = !this.onDisplay;
    }
}