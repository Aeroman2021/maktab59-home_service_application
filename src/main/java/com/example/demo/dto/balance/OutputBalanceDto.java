package com.example.demo.dto.balance;

import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutputBalanceDto {
    private Integer id;
    private Date transactionDate;
    private Integer SuggestionId;
    private Double cost;
}
