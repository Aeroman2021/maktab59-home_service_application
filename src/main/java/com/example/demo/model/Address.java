package com.example.demo.model;

import lombok.*;

import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Address {
    private String province;
    private String city;
    private String street;
    private String Alley;
    private Integer houseNumber;
    private String postalCode;

}
