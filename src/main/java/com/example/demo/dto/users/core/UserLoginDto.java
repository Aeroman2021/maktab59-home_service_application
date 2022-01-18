package com.example.demo.dto.users.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {
    private Integer id;
    private String username;
    private String password;
}
