package com.example.demo.controller;


import com.example.demo.controller.users.TechnicianRestController;
import com.example.demo.dto.order.OutputOrderInformationDto;
import com.example.demo.dto.subServices.SubServiceOutputDto;
import com.example.demo.dto.users.core.UserOutputDto;
import com.example.demo.model.Technician;
import com.example.demo.model.enums.SubServicesName;
import com.example.demo.service.OrderService;
import com.example.demo.service.SubServicesService;
import com.example.demo.service.SuggestionService;
import com.example.demo.service.TechnicianService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(TechnicianRestController.class)
@ActiveProfiles("test")
public class TechnicianRestControllerTest extends AbstractRestControllerTest {

    @MockBean
    private TechnicianService technicianService;
    @MockBean
    private SubServicesService subServicesService;
    @MockBean
    private OrderService orderService;
    @MockBean
    private SuggestionService suggestionService;

    @Test
    public void loadTechnicianByName_isOK() throws Exception {
        List<UserOutputDto> resultList = new ArrayList<>();
        UserOutputDto user1 = UserOutputDto.builder()
                .id(12)
                .firstName("Ali")
                .lastname("Taghavi")
                .build();
        resultList.add(user1);
        Mockito.when(technicianService.loadTechsByFirstName("Ali")).thenReturn(resultList);

        mvc.perform(get("/technicians/technicians/firstname/Ali"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.dataList[0].id").value(resultList.get(0).getId()))
                .andExpect(jsonPath("$.dataList[0].firstName").value(resultList.get(0).getFirstName()))
                .andExpect(jsonPath("$.dataList[0].lastname").value(resultList.get(0).getLastname()));
    }

    @Test
    public void loadTechnicianByLastName_isOK() throws Exception {
        List<UserOutputDto> resultList = new ArrayList<>();
        UserOutputDto user1 = UserOutputDto.builder()
                .id(12)
                .firstName("Ali")
                .lastname("Taghavi")
                .build();
        resultList.add(user1);
        Mockito.when(technicianService.loadTechsByByLastname("Taghavi")).thenReturn(resultList);

        mvc.perform(get("/technicians/technicians/lastname/Taghavi"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.dataList[0].id").value(resultList.get(0).getId()))
                .andExpect(jsonPath("$.dataList[0].firstName").value(resultList.get(0).getFirstName()))
                .andExpect(jsonPath("$.dataList[0].lastname").value(resultList.get(0).getLastname()));
    }

    @Test
    public void loadTechniciansByEmail_isOK() throws Exception {
        Technician technician = Technician.builder()
                .firstName("Mohsen")
                .lastName("Malakouti")
                .email("malakouti.aero@gmail.com")
                .username("Aeroman2022")
                .password("Aeroman789654")
                .build();

        UserOutputDto outputDto = UserOutputDto.builder()
                .id(1).firstName("Mohsen").lastname("Malakouti").build();
        Mockito.when(technicianService.filterTechsByEmail("malakouti.aero@gmail.com")).thenReturn(outputDto);

        mvc.perform(get("/technicians/technicians/email/malakouti.aero@gmail.com")
                        .content(toJson(technician))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").value(outputDto.getId()))
                .andExpect(jsonPath("$.data.lastname").value(outputDto.getLastname()))
                .andExpect(jsonPath("$.data.firstName").value(outputDto.getFirstName()));
    }

    @Test
    public void listTechniciansByPoint_isOK() throws Exception {
        List<UserOutputDto> userOutputDtos = new ArrayList<>();
        Technician technician1 = Technician.builder()
                .firstName("Mohsen")
                .lastName("Malakouti")
                .email("malakouti.aero@gmail.com")
                .username("Aeroman2022")
                .password("Aeroman789654")
                .averagePoint(6.0)
                .build();
        UserOutputDto userOutputDto1 = UserOutputDto.builder()
                .id(1)
                .firstName("Mohsen")
                .lastname("Malakouti")
                .build();
        userOutputDtos.add(userOutputDto1);

        Technician technician2 = Technician.builder()
                .firstName("Mohsen")
                .lastName("Ahmadi")
                .email("ahmadi.aero@gmail.com")
                .username("ahmadi20222021")
                .password("mohsenAhmadi2022")
                .averagePoint(7.0)
                .build();

        UserOutputDto userOutputDto2 = UserOutputDto.builder()
                .id(2)
                .firstName("Mohsen")
                .lastname("Ahmadi")
                .build();
        userOutputDtos.add(userOutputDto2);

        Mockito.when(technicianService.loadTechsWithPointsGreaterThanLimit(5.0)).thenReturn(userOutputDtos);

        mvc.perform(get("/technicians/technicians/point/5")
                        .content(toJson(technician1))
                        .content(toJson(technician2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.dataList[0].id").value(userOutputDtos.get(0).getId()))
                .andExpect(jsonPath("$.dataList[0].firstName").value(userOutputDtos.get(0).getFirstName()))
                .andExpect(jsonPath("$.dataList[0].lastname").value(userOutputDtos.get(0).getLastname()))
                .andExpect(jsonPath("$.dataList[1].id").value(userOutputDtos.get(1).getId()))
                .andExpect(jsonPath("$.dataList[1].firstName").value(userOutputDtos.get(1).getFirstName()))
                .andExpect(jsonPath("$.dataList[1].lastname").value(userOutputDtos.get(1).getLastname()));
    }


    @Test
    public void loadRelatedOrderListForTechnicians_isOK() throws Exception {
        List<OutputOrderInformationDto> resultList = new ArrayList<>();
        OutputOrderInformationDto orderInformationDto = OutputOrderInformationDto.builder()
                .orderId(1)
                .customerId(1)
                .serviceId(1)
                .build();

        resultList.add(orderInformationDto);
        Mockito.when(orderService.listRelatedOrdersForTechnicians(1))
                .thenReturn(resultList);

        mvc.perform(get("/technicians/List/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.dataList[0].orderId").value(resultList.get(0).getOrderId()))
                .andExpect(jsonPath("$.dataList[0].customerId").value(resultList.get(0).getCustomerId()))
                .andExpect(jsonPath("$.dataList[0].serviceId").value(resultList.get(0).getServiceId()))
                .andExpect(jsonPath("$.dataList[0].orderStatus").value(resultList.get(0).getOrderStatus()));
    }

    @Test
    public void addTechnicianToSubservice_isOK() throws Exception {
        SubServiceOutputDto subServiceOutputDto = SubServiceOutputDto.builder()
                .subServicesName(SubServicesName.Building_Spraying)
                .id(2)
                .price(85000.0)
                .build();

        Mockito.when(subServicesService
                        .submitTechnicianToSubService(2,1))
                .thenReturn(subServiceOutputDto);

        mvc.perform(put("/technicians/submit/technicians/1/subservices/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").value(subServiceOutputDto.getId()))
                .andExpect(jsonPath("$.data.price").value(subServiceOutputDto.getPrice()));

    }


}
