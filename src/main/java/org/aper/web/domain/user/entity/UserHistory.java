package org.aper.web.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import org.aper.web.domain.user.entity.constant.HistoryTypeEnum;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "user_history")
public class UserHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HistoryTypeEnum historyType;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String description;

    public void updateHistory(LocalDate date, LocalDate endDate, String description, HistoryTypeEnum historyType) {
        this.date = date;
        this.description = description;
        this.historyType = historyType;

        if (endDate != null) {
            this.endDate = endDate;
        }
    }

    public void updateUser(User user){
        this.user = user;
    }

}
