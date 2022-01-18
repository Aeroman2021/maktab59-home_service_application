package com.example.demo.dto.subServices;

import com.example.demo.model.enums.SubServicesName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubServiceOutputDto {
    private Integer id;
    private SubServicesName subServicesName ;
    private Double price;
}
