package com.example.demo.dto.mainServices;


import com.example.demo.model.enums.MainServicesName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MainServiceOutputDto {
    private Integer id;
    private MainServicesName name;
}
