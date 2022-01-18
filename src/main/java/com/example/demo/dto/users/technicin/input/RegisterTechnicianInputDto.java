package com.example.demo.dto.users.technicin.input;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterTechnicianInputDto {
    private String firstName;

    private String lastName;

    private String email;

    private String username;

    private String password;
    // transaction
    private Double Credit;
}
