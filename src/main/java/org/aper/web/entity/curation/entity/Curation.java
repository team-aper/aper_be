package org.aper.web.entity.curation.entity;


import org.aper.web.entity.episode.entity.Episode;
import org.aper.web.entity.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "curations")
@NoArgsConstructor
public class Curation extends BaseEntity {
    @NotBlank(message = "content is blank")
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "episode_id")
    private Episode episode;

    @Builder
    public Curation(Episode episode) {
        this.content = episode.getDescription();
        this.episode = episode;
        this.createdAt = LocalDateTime.now();
    }
}