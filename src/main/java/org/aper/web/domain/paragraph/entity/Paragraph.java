package org.aper.web.domain.paragraph.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aper.web.domain.episode.entity.Episode;

@Entity
@Getter
@NoArgsConstructor
public class Paragraph {

    @Id
    private String uuid;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    private String textAlign;

    private String previousUuid;
    private String nextUuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episode_id", nullable = false)
    private Episode episode;

    @Builder
    public Paragraph(String uuid, String content, String textAlign, String previousUuid, String nextUuid, Episode episode) {
        this.uuid = uuid;
        this.content = content;
        this.previousUuid = previousUuid;
        this.nextUuid = nextUuid;
        this.episode = episode;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updatePreviousUuid(String previousUuid) {
        this.previousUuid = previousUuid;
    }

    public void updateNextUuid(String nextUuid) {
        this.nextUuid = nextUuid;
    }

    public void updateTextAlign(String textAlign) { this.textAlign = textAlign; }
}
