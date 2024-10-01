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

    @Column(nullable = false)
    private String content;

    private String previousUuid;
    private String nextUuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episode_id", nullable = false)
    private Episode episode;

    @Builder
    public Paragraph(String uuid, String content, String previousUuid, String nextUuid, Episode episode) {
        this.uuid = uuid;
        this.content = content;
        this.previousUuid = previousUuid;
        this.nextUuid = nextUuid;
        this.episode = episode;
    }

    // 문단 업데이트 메서드 (상태 변경만 관리)
    public void updateContent(String content) {
        this.content = content;
    }

    public void updatePreviousUuid(String previousUuid) {
        this.previousUuid = previousUuid;
    }

    public void updateNextUuid(String nextUuid) {
        this.nextUuid = nextUuid;
    }
}
