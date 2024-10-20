package org.aper.web.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import org.aper.web.domain.user.entity.constant.EndDateTypeEnum;
import org.aper.web.domain.user.entity.constant.HistoryTypeEnum;
import org.aper.web.domain.user.entity.constant.StartDateTypeEnum;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;

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

    @Enumerated(EnumType.STRING)
    @Column
    private EndDateTypeEnum endDateType;

    @Enumerated(EnumType.STRING)
    @Column
    private StartDateTypeEnum startDateType;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    private YearMonth date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    private YearMonth endDate;

    private String description;

    public void updateEducation(
            YearMonth date,
            YearMonth endDate,
            String description,
            EndDateTypeEnum endDateType,
            StartDateTypeEnum startDateType) {
        this.date = date;
        this.description = description;
        this.historyType = HistoryTypeEnum.EDUCATION;
        this.endDate = endDate;
        this.endDateType = endDateType;
        this.startDateType = startDateType;
    }

    public void updateAwardPublication(YearMonth date, String description, HistoryTypeEnum historyType) {
        this.date = date;
        this.description = description;
        this.historyType = historyType;
    }

    public void updateUser(User user){
        this.user = user;
    }

}
