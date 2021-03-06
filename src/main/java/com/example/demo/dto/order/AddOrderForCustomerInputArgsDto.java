package com.example.demo.dto.order;


import com.example.demo.dto.address.SubmittedAddressDto;
import com.example.demo.dto.creditcard.CreditCardInputDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddOrderForCustomerInputArgsDto {
    private Integer customerId;
    private Integer subServiceId;
    private Integer SuggestionId;
    private CreditCardInputDto creditCardInputDto;
    private SubmittedAddressDto submittedAddressDto;
    private InputOrderInformationDto submittedOrderDto;

}
