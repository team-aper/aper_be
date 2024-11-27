package com.aperlibrary.paragraph.entity;

import com.aperlibrary.episode.entity.Episode;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Paragraph {

    @Id
    private String uuid;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    private TextAlignEnum textAlign;

    private String previousUuid;
    private String nextUuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episode_id", nullable = false)
    private Episode episode;

    @Builder
    public Paragraph(String uuid, String content, TextAlignEnum textAlign, String previousUuid, String nextUuid, Episode episode) {
        this.uuid = uuid;
        this.content = content;
        this.textAlign = textAlign;
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

    public void updateTextAlign(TextAlignEnum textAlign) { this.textAlign = textAlign; }

    public void assignEpisode(Episode episode) {
        this.episode = episode;
    }
}
