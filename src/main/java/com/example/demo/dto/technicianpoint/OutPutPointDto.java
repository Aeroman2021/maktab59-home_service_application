package com.example.demo.dto.technicianpoint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutPutPointDto {
    private Integer technicianId;
    private Integer point;
}
