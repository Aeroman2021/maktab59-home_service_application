package com.example.demo.service;

import com.example.demo.dto.users.core.UserOutputDto;
import com.example.demo.dto.users.customer.input.RegisterCustomerInputDto;
import com.example.demo.model.Technician;
import com.example.demo.model.TechnicianPoint;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TechnicianService technicianService;

    @Autowired
    private TechnicianPointService technicianPointService;


    @Test
    void addCustomer() {
        RegisterCustomerInputDto customerInputDto = RegisterCustomerInputDto.builder()
                .firstName("Ali")
                .lastName("Akbari")
                .username("AliAhmadi")
                .password("12345678asd")
                .email("ali.ahma@gmail.com")
                .Credit(0.0)
                .build();

        UserOutputDto userOutputDto = customerService.addCustomer(customerInputDto);
        System.out.println(userOutputDto.getId());
    }

    @Test
    void setPointToTechnician_isOK() {
        Technician technician = technicianService.loadById(1);
        TechnicianPoint technicianPoint1 = TechnicianPoint.builder()
                .technician(technician)
                .point(6)
                .build();
        technicianPointService.save(technicianPoint1);

        TechnicianPoint technicianPoint2 = TechnicianPoint.builder()
                .technician(technician)
                .point(8)
                .build();
        technicianPointService.save(technicianPoint2);
        customerService.setPointForTheTechnicians(technicianPoint1);
        customerService.setPointForTheTechnicians(technicianPoint2);
        assertEquals(7, technician.getAveragePoint());

    }
}