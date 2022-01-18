package com.example.demo.dto.subServices;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitTechsToSubServicesOutputDto {
    private Integer subServiceId;
    private Integer technicianId;
}
