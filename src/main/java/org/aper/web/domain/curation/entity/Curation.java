package org.aper.web.domain.curation.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.global.entity.BaseEntity;

@Entity
@Getter
@Table(name = "curations")
public class Curation extends BaseEntity {
    @NotBlank(message = "field title is blank")
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "episode_id")
    private Episode episode;

    public Curation() {
    }
}