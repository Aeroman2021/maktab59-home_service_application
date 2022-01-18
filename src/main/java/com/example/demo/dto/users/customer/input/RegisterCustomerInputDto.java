package com.example.demo.dto.users.customer.input;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterCustomerInputDto {

    private String firstName;

    private String lastName;

    private String email;

    private String username;

    private String password;
    // transaction
    private Double Credit;


}
