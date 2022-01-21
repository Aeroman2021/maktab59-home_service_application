package com.example.demo.controller.users;

import com.example.demo.controller.api.ResponseResult;
import com.example.demo.dto.order.OutputOrderInformationDto;
import com.example.demo.dto.subServices.SubServiceOutputDto;
import com.example.demo.dto.suggestion.SuggestionInputDto;
import com.example.demo.dto.suggestion.SuggestionOutputDto;
import com.example.demo.dto.users.core.UserOutputDto;
import com.example.demo.dto.users.technicin.input.RegisterTechnicianInputDto;
import com.example.demo.model.Suggestion;
import com.example.demo.service.OrderService;
import com.example.demo.service.SubServicesService;
import com.example.demo.service.SuggestionService;
import com.example.demo.service.TechnicianService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/technicians")
public class TechnicianRestController {

    private final TechnicianService technicianService;
    private final SubServicesService subServicesService;
    private final OrderService orderService;
    private final SuggestionService suggestionService;

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
    public ResponseEntity<ResponseResult<UserOutputDto>> filterTechsByPoint(@PathVariable Double point) {
        List<UserOutputDto> techs = technicianService.loadTechsWithPointsGreaterThanLimit(point);
        return ResponseEntity.ok(ResponseResult.<UserOutputDto>builder()
                .code(0)
                .dataList(techs)
                .message("The technician list loaded successfully...")
                .build()
        );
    }

    @PutMapping ("/submit/technicians/{technicianId}/subservices/{subserviceId}")
    public ResponseEntity<ResponseResult<SubServiceOutputDto>>
    submitTechToSubService( @PathVariable Integer subserviceId,@PathVariable Integer technicianId ){
        SubServiceOutputDto subServiceOutputDto =
                subServicesService.submitTechnicianToSubService(subserviceId,technicianId);
        return ResponseEntity.ok(ResponseResult.<SubServiceOutputDto>builder()
                .code(0)
                .data(subServiceOutputDto)
                .message("The technician submitted to the subservice successfully.")
                .build());
    }


    @PostMapping
    public ResponseEntity<ResponseResult<UserOutputDto>> saveTechnician
            (@RequestBody RegisterTechnicianInputDto technicianInputDto) {
        UserOutputDto userOutputDto = technicianService.addTechnician(technicianInputDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseResult.<UserOutputDto>builder()
                        .code(0)
                        .data(userOutputDto)
                        .message("technicain added successfully.")
                        .build());
    }

    @PostMapping("/technicinas")
    public ResponseEntity<ResponseResult<SuggestionOutputDto>> saveSuggestion
            (@RequestBody SuggestionInputDto suggestionInputDto) {
        SuggestionOutputDto suggestionOutputDto = suggestionService.submitSuggestionByTechnicians(suggestionInputDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseResult.<SuggestionOutputDto>builder()
                        .code(0)
                        .data(suggestionOutputDto)
                        .message("The suggestion added successfully.")
                        .build());
    }


    @GetMapping("/List/subservices")
    public ResponseEntity<ResponseResult<SubServiceOutputDto>> saveMainService() {
        List<SubServiceOutputDto> subServiceOutputDtos = subServicesService.loadAllSubServices();
        return ResponseEntity.ok(ResponseResult.<SubServiceOutputDto>builder()
                .code(0)
                .dataList(subServiceOutputDtos)
                .message(" The subservices List loaded successfully.")
                .build());
    }

    @GetMapping("/List/orders/{technicianId}")
    public ResponseEntity<ResponseResult<OutputOrderInformationDto>>
    printRelatedOrderList(@PathVariable Integer technicianId) {
        List<OutputOrderInformationDto> orderInformationDtos =
                orderService.listRelatedOrdersForTechnicians(technicianId);
        return ResponseEntity.ok(ResponseResult.<OutputOrderInformationDto>builder()
                .code(0)
                .dataList(orderInformationDtos)
                .message(" The order List loaded successfully.")
                .build());
    }






}
