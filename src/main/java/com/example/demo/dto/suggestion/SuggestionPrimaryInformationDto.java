package com.example.demo.dto.suggestion;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuggestionPrimaryInformationDto {
    private Date registerSuggestion;
    private double suggestedPrice;
    private Integer workDuration;
    private Integer startHour;
}
