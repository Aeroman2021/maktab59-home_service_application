package com.example.demo.model;

import com.example.demo.model.enums.SuggestionStatus;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table(name = "suggestions")
public class Suggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "register_date")
    private Date registerSuggestion;

    @Column(name = "suggested_price")
    private double suggestedPrice;

    @Column(name = "work_duration")
    private Integer workDuration;

    @Column(name = "start_time")
    private Integer startHour;

    @ManyToOne
    private Technician technician;

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Order order;

    @Enumerated(EnumType.STRING)
    private SuggestionStatus status = SuggestionStatus.Pending;

    @Override
    public String toString() {
        return "Suggestion{" +
                "id=" + id +
                ", registerSuggestion=" + registerSuggestion +
                ", suggestedPrice=" + suggestedPrice +
                ", workDuration=" + workDuration +
                ", startHour=" + startHour +
                ", technician=" + technician +
                ", order=" + order +
                ", status=" + status +
                '}';
    }
}
