package com.example.demo.dto.address;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmittedAddressDto {
    private String province;
    private String city;
    private String street;
    private String alley;
    private Integer homeNumber;
    private String postalCode;

}
