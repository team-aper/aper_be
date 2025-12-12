package org.aper.web.domain.paragraph.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aper.web.domain.common.constant.TextAlignEnum;
import org.aper.web.domain.episode.entity.Episode;

@Entity
@Getter
@Table(name = "paragraphs")
@NoArgsConstructor
public class Paragraph {

    @Id
    @Column(length = 100)
    private String uuid;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "text_align")
    private TextAlignEnum textAlign;

    @Column(name = "previous_uuid", length = 100)
    private String previousUuid;

    @Column(name = "next_uuid", length = 100)
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

    public void updateTextAlign(TextAlignEnum textAlign) {
        this.textAlign = textAlign;
    }

    public void assignEpisode(Episode episode) {
        this.episode = episode;
    }
}
