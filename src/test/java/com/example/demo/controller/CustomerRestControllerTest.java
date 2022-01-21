package com.example.demo.controller;


import com.example.demo.controller.users.CustomerRestController;
import com.example.demo.dto.users.core.UserOutputDto;
import com.example.demo.model.Customer;
import com.example.demo.service.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerRestController.class)
@ActiveProfiles("test")
public class CustomerRestControllerTest extends AbstractRestControllerTest {

    @MockBean
    private CustomerService customerService;
    @MockBean
    private SubServicesService subServicesService;
    @MockBean
    private OrderService orderService;
    @MockBean
    private CommentService commentService;
    @MockBean
    private SuggestionService suggestionService;

    @Test
    public void RegisterCustomer_isOK() throws Exception {
        Customer customer = Customer.builder()
                .firstName("Mohsen")
                .lastName("Malakouti")
                .email("malakouti.aero@gmail.com")
                .username("Aeroman2022")
                .password("Aeroman789654")
                .build();

        UserOutputDto outputDto = UserOutputDto.builder()
                .id(1).firstName("Mohsen").lastname("Malakouti").build();
        Mockito.when(customerService.addCustomer(any())).thenReturn(outputDto);

        mvc.perform(post("/customers/addCustomer")
                        .content(toJson(customer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.data.id").value(outputDto.getId()))
                        .andExpect(jsonPath("$.data.lastname").value(outputDto.getLastname()))
                        .andExpect(jsonPath("$.data.firstName").value(outputDto.getFirstName()));
    }

    @Test
    public void loadCustomerByName_isOK() throws Exception {
        List<UserOutputDto> resultList = new ArrayList<>();
        UserOutputDto user1 = UserOutputDto.builder()
                .id(12)
                .firstName("Ali")
                .lastname("Taghavi")
                .build();
        resultList.add(user1);
        Mockito.when(customerService.loadCustomerByFirstName("Ali")).thenReturn(resultList);

        mvc.perform(get("/customers/customers/firstname/Ali"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.dataList[0].id").value(resultList.get(0).getId()))
                .andExpect(jsonPath("$.dataList[0].firstName").value(resultList.get(0).getFirstName()))
                .andExpect(jsonPath("$.dataList[0].lastname").value(resultList.get(0).getLastname()));
    }

    @Test
    public void loadCustomerByLastName_isOK() throws Exception {
        List<UserOutputDto> resultList = new ArrayList<>();
        UserOutputDto user1 = UserOutputDto.builder()
                .id(12)
                .firstName("Ali")
                .lastname("Taghavi")
                .build();
        resultList.add(user1);
        Mockito.when(customerService.loadCustomerByLastname("Taghavi")).thenReturn(resultList);

        mvc.perform(get("/customers/customers/lastname/Taghavi"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.dataList[0].id").value(resultList.get(0).getId()))
                .andExpect(jsonPath("$.dataList[0].firstName").value(resultList.get(0).getFirstName()))
                .andExpect(jsonPath("$.dataList[0].lastname").value(resultList.get(0).getLastname()));
    }

    @Test
    public void loadCustomerByEmail_isOK() throws Exception {
        Customer customer = Customer.builder()
                .firstName("Mohsen")
                .lastName("Malakouti")
                .email("malakouti.aero@gmail.com")
                .username("Aeroman2022")
                .password("Aeroman789654")
                .build();

        UserOutputDto outputDto = UserOutputDto.builder()
                .id(1).firstName("Mohsen").lastname("Malakouti").build();

        Mockito.when(customerService.filterCustomerByEmail("malakouti.aero@gmail.com")).thenReturn(outputDto);

        mvc.perform(get("/customers/customers/email/malakouti.aero@gmail.com")
                        .content(toJson(customer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").value(outputDto.getId()))
                .andExpect(jsonPath("$.data.lastname").value(outputDto.getLastname()))
                .andExpect(jsonPath("$.data.firstName").value(outputDto.getFirstName()));
    }

}
