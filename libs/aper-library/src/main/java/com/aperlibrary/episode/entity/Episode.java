package com.aperlibrary.episode.entity;


import com.aperlibrary.global.entity.BaseSoftDeleteEntity;
import com.aperlibrary.paragraph.entity.Paragraph;
import com.aperlibrary.story.entity.Story;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "episodes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Episode extends BaseSoftDeleteEntity {
    @Column
    private Long chapter;

    @Schema(description = "Post Title", nullable = true)
    private String title;

    @Column(name = "on_display", columnDefinition = "boolean default false")
    private boolean onDisplay;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime publicDate;

    @Schema(description = "Episode Description", nullable = true)
    @Column(columnDefinition = "TEXT", name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "story_id")
    private Story story;

    @Schema(description = "Episode Description's Paragraphs", nullable = true)
    @OneToMany(mappedBy = "episode", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Paragraph> paragraphs = new ArrayList<>();

    @Builder
    public Episode(Long chapter, String title, String description, Story story, List<Paragraph> paragraphs) {
        this.chapter = chapter;
        this.title = title;
        this.description = description;
        this.story = story;
        if (paragraphs != null) {
            this.paragraphs = paragraphs;
            for (Paragraph paragraph : paragraphs) {
                paragraph.assignEpisode(this);
            }
        }
    }

    public void updateOnDisplay() {
        if (!this.isOnDisplay()){
            this.publicDate = LocalDateTime.now();
        }
        this.onDisplay = !this.onDisplay;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateDescription(String truncatedParagraph) {
        this.description = truncatedParagraph;
    }

}