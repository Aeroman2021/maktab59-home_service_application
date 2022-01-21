package com.example.demo.dto.balance;

import com.example.demo.dto.creditcard.CreditCardInputDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputBalanceDto {
    private CreditCardInputDto creditCardInputDto;
    private Integer orderId;
    private Integer customerId;
    private Integer technicianId;
    private Integer suggestionId;

}
