package com.example.demo.dto.creditcard;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditCardInputDto {
    private Long cardNumber;
    private Integer cvv2;
    private Date expDate;
    private String password;
    private Integer customerId;
}
