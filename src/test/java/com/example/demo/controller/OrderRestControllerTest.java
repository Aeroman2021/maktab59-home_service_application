package com.example.demo.controller;


import com.example.demo.controller.order.OrderRestController;
import com.example.demo.dto.address.SubmittedAddressDto;
import com.example.demo.dto.creditcard.CreditCardInputDto;
import com.example.demo.dto.order.AddOrderForCustomerInputArgsDto;
import com.example.demo.dto.order.InputOrderInformationDto;
import com.example.demo.dto.order.OutputOrderInformationDto;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(OrderRestController.class)
@ActiveProfiles("test")
public class OrderRestControllerTest extends AbstractRestControllerTest {

    @MockBean
    private OrderService orderService;


    @Test
    public void submitOrder_isOK(){
        CreditCardInputDto creditCardInputDto = CreditCardInputDto.builder()
                .cardNumber(1234789456123654L)
                .customerId(1)
                .cvv2(969)
                .password("7894")
                .expDate(new Date(122,4,4))
                .build();


        InputOrderInformationDto inputOrderInformationDto  = InputOrderInformationDto.builder()
                .Description("very important")
                .suggestedPrice(48000d)
                .startDate(new Date(122,4,10))
                .build();

        SubmittedAddressDto submittedAddressDto = SubmittedAddressDto.builder()
                .city("Bushehr")
                .alley("Laleh_19")
                .postalCode("123654")
                .province("Bushehr")
                .street("Bisim")
                .homeNumber(7)
                .build();

        AddOrderForCustomerInputArgsDto  orderInputArgsDto = AddOrderForCustomerInputArgsDto.builder()
                .creditCardInputDto(creditCardInputDto)
                .submittedAddressDto(submittedAddressDto)
                .submittedOrderDto(inputOrderInformationDto)
                .customerId(1)
                .subServiceId(1)
                .SuggestionId(1)
                .build();


        OutputOrderInformationDto result = OutputOrderInformationDto.builder()
                .serviceId(1)
                .orderId(1)
                .customerId(1)
                .build();

        Mockito.when(orderService.addOrderForCustomer(orderInputArgsDto)).thenReturn(result);

//        mvc.perform(get("/technicians/technicians/email/malakouti.aero@gmail.com")
//                        .content(toJson(technician))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().is(200))
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.data.id").value(outputDto.getId()))
//                .andExpect(jsonPath("$.data.lastname").value(outputDto.getLastname()))
//                .andExpect(jsonPath("$.data.firstName").value(outputDto.getFirstName()));




    }

    @Test
    public void loadSubmittedOrderByCustomer_isOK() throws Exception {
        List<OutputOrderInformationDto> resultList = new ArrayList<>();
        OutputOrderInformationDto orderInformationDto = OutputOrderInformationDto.builder()
                .orderId(1)
                .customerId(1)
                .orderStatus(OrderStatus.FINISHED_THE_PROCESS)
                .serviceId(1)
                .build();
        resultList.add(orderInformationDto);
        Mockito.when(orderService.loadOrdersByCustomerId(1)).thenReturn(resultList);

        mvc.perform(get("/orders/loadlist/submittedorders/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.dataList[0].orderId").value(resultList.get(0).getOrderId()))
                .andExpect(jsonPath("$.dataList[0].customerId").value(resultList.get(0).getCustomerId()))
                .andExpect(jsonPath("$.dataList[0].serviceId").value(resultList.get(0).getServiceId()));
//                .andExpect(jsonPath("$.dataList[0].orderStatus").value(resultList.get(0).getOrderStatus()));
    }
}
