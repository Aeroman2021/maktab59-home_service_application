package com.example.demo.controller.users;

import com.example.demo.controller.api.ResponseResult;
import com.example.demo.dto.subServices.SubServiceOutputDto;
import com.example.demo.dto.subServices.SubmitTechsToSubServicesInputDto;
import com.example.demo.dto.users.core.UserOutputDto;
import com.example.demo.service.CustomerService;
import com.example.demo.service.SubServicesService;
import com.example.demo.service.TechnicianService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminRestController {

    private final CustomerService customerService;
    private final TechnicianService technicianService;
    private final SubServicesService subServicesService;

    // http://localhost:8080/Customer/firstname?firstName=Ali
    @GetMapping("/customers/firstname/{firstName}")
    public ResponseEntity<ResponseResult<UserOutputDto>> filterCustsByFirstName(@PathVariable String firstName) {
        List<UserOutputDto> customers = customerService.loadCustomerByFirstName(firstName);
        return ResponseEntity.ok(ResponseResult.<UserOutputDto>builder()
                .code(0)
                .dataList(customers)
                .message("The customer list loaded successfully...")
                .build()
        );
    }

    // http://localhost:8080/Customer/Alavi
    @GetMapping("/customers/lastname/{lastName}")
    public ResponseEntity<ResponseResult<UserOutputDto>> filterCustsByLastName(@PathVariable String lastName) {
        List<UserOutputDto> customers = customerService.loadCustomerByLastname(lastName);
        return ResponseEntity.ok(ResponseResult.<UserOutputDto>builder()
                .code(0)
                .dataList(customers)
                .message("The customer list loaded successfully...")
                .build()
        );
    }

    @GetMapping("/customers/email/{email}")
    public ResponseEntity<ResponseResult<UserOutputDto>> filterCustsByEmail(@PathVariable String email) {
        UserOutputDto customer = customerService.filterCustomerByEmail(email);
        return ResponseEntity.ok(ResponseResult.<UserOutputDto>builder()
                .code(0)
                .data(customer)
                .message("The customer loaded successfully")
                .build()
        );
    }

    @GetMapping("/technicians/firstname/{firstName}")
    public ResponseEntity<ResponseResult<UserOutputDto>> filterTechsByFirstName(@PathVariable String firstName) {
        List<UserOutputDto> techs = technicianService.loadTechsByFirstName(firstName);
        return ResponseEntity.ok(ResponseResult.<UserOutputDto>builder()
                .code(0)
                .dataList(techs)
                .message("The technician list loaded successfully...")
                .build()
        );
    }

    @GetMapping("/technicians/lastname/{lastName}")
    public ResponseEntity<ResponseResult<UserOutputDto>> filterTechsByLastName(@PathVariable String lastName) {
        List<UserOutputDto> techs = technicianService.loadTechsByByLastname(lastName);
        return ResponseEntity.ok(ResponseResult.<UserOutputDto>builder()
                .code(0)
                .dataList(techs)
                .message("The technician list loaded successfully...")
                .build()
        );
    }

    @GetMapping("/technicians/email/{email}")
    public ResponseEntity<ResponseResult<UserOutputDto>> filterTechsByEmail(@PathVariable String email) {
        UserOutputDto technician = technicianService.filterTechsByEmail(email);
        return ResponseEntity.ok(ResponseResult.<UserOutputDto>builder()
                .code(0)
                .data(technician)
                .message("The technician loaded successfully...")
                .build()
        );
    }

    @GetMapping("/technicians/point/{point}")
    public ResponseEntity<ResponseResult<UserOutputDto>> filterTechsByPoint(@PathVariable Integer point) {
        List<UserOutputDto> techs = technicianService.loadTechsWithPointsGreaterThanLimit(point);
        return ResponseEntity.ok(ResponseResult.<UserOutputDto>builder()
                .code(0)
                .dataList(techs)
                .message("The technician list loaded successfully...")
                .build()
        );
    }

    @PostMapping("/submit/technicians/{technicianId}/subservices/{subserviceId}")
    public ResponseEntity<ResponseResult<SubServiceOutputDto>>
    submitTechToSubService(@RequestBody SubmitTechsToSubServicesInputDto submitTechsToSubServicesInputDto){
        SubServiceOutputDto subServiceOutputDto =
                subServicesService.submitTechnicianToSubService(submitTechsToSubServicesInputDto);
        return ResponseEntity.ok().body(ResponseResult.<SubServiceOutputDto>builder()
                .code(0)
                .data(subServiceOutputDto)
                .message("The technician submitted to the subservice successfully.")
                .build());
    }




}
