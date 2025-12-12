package org.aper.web.domain.curation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aper.web.domain.common.entity.BaseEntity;
import org.aper.web.domain.episode.entity.Episode;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "curations")
@NoArgsConstructor
public class Curation extends BaseEntity {

    @NotBlank(message = "content is blank")
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episode_id")
    private Episode episode;

    @Builder
    public Curation(Episode episode) {
        this.content = episode.getDescription();
        this.episode = episode;
        this.createdAt = LocalDateTime.now();
    }
}
