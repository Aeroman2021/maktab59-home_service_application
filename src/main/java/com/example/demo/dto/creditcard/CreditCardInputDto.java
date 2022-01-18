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
    private Long CardNumber;
    private Integer CVV2;
    private Date expDate;
    private String password;
    private Integer customerId;
}
