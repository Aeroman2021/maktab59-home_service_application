package com.example.demo.controller.subservice;

import com.example.demo.controller.api.ResponseResult;
import com.example.demo.dto.subServices.RegisterSubServiceInputDto;
import com.example.demo.dto.subServices.SubServiceOutputDto;
import com.example.demo.service.SubServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subservices")
@RequiredArgsConstructor
public class SubServiceRestController {

    private final SubServicesService subServices;

    @PostMapping
    public ResponseEntity<ResponseResult<SubServiceOutputDto>> saveSubService
            (@RequestBody RegisterSubServiceInputDto subServiceInputDto){
        SubServiceOutputDto subServiceOutputDto = subServices.addSubService(subServiceInputDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseResult.<SubServiceOutputDto>builder()
                .code(0)
                .data(subServiceOutputDto)
                .message(" The service added successfully.")
                .build());
    }


}
