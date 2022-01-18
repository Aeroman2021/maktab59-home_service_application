package com.example.demo.dto.users.core;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public  class UserOutputDto {
    private Integer id;
    private String firstName;
    private String lastname;
}
