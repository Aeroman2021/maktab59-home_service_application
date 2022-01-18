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
public class RegisterSubServiceInputDto {
    private SubServicesName name;
    private String description;
    private double basePrice;
    private Integer mainServicesId;
}
