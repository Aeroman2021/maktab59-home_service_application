package com.example.demo.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputOrderInformationDto {
    private Double suggestedPrice;
    private String Description;
    private Date startDate;
}
