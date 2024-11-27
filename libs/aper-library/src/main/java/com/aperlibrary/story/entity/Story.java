package com.aperlibrary.story.entity;

import com.aperlibrary.episode.entity.Episode;
import com.aperlibrary.global.entity.BaseSoftDeleteEntity;
import com.aperlibrary.story.entity.constant.StoryGenreEnum;
import com.aperlibrary.story.entity.constant.StoryLineStyleEnum;
import com.aperlibrary.story.entity.constant.StoryRoutineEnum;
import com.aperlibrary.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "stories")
@NoArgsConstructor
public class Story extends BaseSoftDeleteEntity {
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

    // 에피소드 리스트 초기화
    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Episode> episodeList = new ArrayList<>();

    @Builder
    public Story(String title, StoryRoutineEnum routine, StoryGenreEnum genre, StoryLineStyleEnum lineStyle, User user) {
        this.title = title;
        this.routine = routine;
        this.genre = genre;
        this.lineStyle = lineStyle;
        this.user = user;
        this.episodeList = new ArrayList<>();
    }

    public void updateOnDisplay() {
        if (!this.isOnDisplay()){
            this.publicDate = LocalDateTime.now();
        }
        this.onDisplay = !this.onDisplay;
    }

    // 에피소드 추가 메서드
    public void addEpisodes(List<Episode> episodes) {
        this.episodeList.clear();
        this.episodeList.addAll(episodes);
    }

    public void updateCover(String title, StoryGenreEnum genre, StoryLineStyleEnum lineStyle) {
        this.title = title;
        this.genre = genre;
        this.lineStyle = lineStyle;
    }
}
