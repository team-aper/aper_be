package org.aper.web.domain.episode.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.global.entity.BaseSoftDeleteEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "episodes")
public class Episode extends BaseSoftDeleteEntity {
    @Column
    private Long chapter;

    @NotBlank(message = "field title is blank")
    @Schema(description = "Post Title", nullable = false)
    private String title;

    @Column(name = "on_display", columnDefinition = "boolean default false")
    private boolean onDisplay;

    @Column(nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime publicDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "story_id")
    private Story story;

    public Episode() {
    }
}