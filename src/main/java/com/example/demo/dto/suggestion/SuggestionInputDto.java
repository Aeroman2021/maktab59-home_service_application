package com.example.demo.dto.suggestion;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuggestionInputDto {
    private SuggestionPrimaryInformationDto suggestionPrimaryInformationDto;
    private Integer technicianId;
    private Integer orderId;
}
