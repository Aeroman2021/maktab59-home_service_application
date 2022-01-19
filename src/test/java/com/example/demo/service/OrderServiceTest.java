package com.example.demo.service;

import com.example.demo.Exceptions.OrderException;
import com.example.demo.dto.address.SubmittedAddressDto;
import com.example.demo.dto.order.InputOrderInformationDto;
import com.example.demo.dto.order.AddOrderForCustomerInputArgsDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private SubServicesService subServicesService;

    @Autowired
    private CustomerService customerService;


    @Test
    void addOrder_isOk() {
        AddOrderForCustomerInputArgsDto inputArgsDto = new AddOrderForCustomerInputArgsDto();
        SubmittedAddressDto inputAddressDto = SubmittedAddressDto.builder()
                .province("Bushsher")
                .city("Bushsher")
                .street("Bisim")
                .alley("Laleh_19")
                .homeNumber(7)
                .postalCode("1236547")
                .build();

        Date date = new Date(122, 4, 4);
        InputOrderInformationDto submittedOrderDto = InputOrderInformationDto
                .builder()
                .Description("It is very urgent task")
                .suggestedPrice(60000d)
//                .startDate(date)
                .build();

        inputArgsDto.setSubmittedOrderDto(submittedOrderDto);
        inputArgsDto.setSubmittedAddressDto(inputAddressDto);
//        inputArgsDto.setSubServiceId(1);
//        inputArgsDto.setCustomerId(1);
        orderService.addOrderForCustomer(inputArgsDto);
    }

    @Test
    void addOrder_ThrowsException_InsufficientCredit_isOk() {
        AddOrderForCustomerInputArgsDto inputArgsDto = new AddOrderForCustomerInputArgsDto();
        SubmittedAddressDto inputAddressDto = SubmittedAddressDto.builder()
                .province("Bushsher")
                .city("Bushsher")
                .street("Bisim")
                .alley("Laleh_19")
                .homeNumber(7)
                .postalCode("1236547")
                .build();

        Date date = new Date(122, 3, 1);
        InputOrderInformationDto submittedOrderDto = InputOrderInformationDto
                .builder()
                .Description("It is very urgent task")
                .suggestedPrice(10000d)
//                .startDate(date)
                .build();
        inputArgsDto.setSubmittedOrderDto(submittedOrderDto);
        inputArgsDto.setSubmittedAddressDto(inputAddressDto);
//        inputArgsDto.setSubServiceId(8);
//        inputArgsDto.setCustomerId(1);
        assertThrows(OrderException.class, () -> orderService.addOrderForCustomer(inputArgsDto));
    }






}