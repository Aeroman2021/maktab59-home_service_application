package com.example.demo.controller.mainservice;

import com.example.demo.controller.api.ResponseResult;
import com.example.demo.dto.mainServices.MainServiceOutputDto;
import com.example.demo.dto.mainServices.RegisterMainServiceInputDto;
import com.example.demo.service.MainServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mainservices")
@RequiredArgsConstructor
public class MainServiceRestController {
    private final MainServicesService mainServicesService;

    @PostMapping
    public ResponseEntity<ResponseResult<MainServiceOutputDto>> saveMainService
            (@RequestBody RegisterMainServiceInputDto mainServiceInputDto){
        MainServiceOutputDto mainServiceOutputDto = mainServicesService.addMainService(mainServiceInputDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseResult.<MainServiceOutputDto>builder()
                .code(0)
                .data(mainServiceOutputDto)
                .message(" The MainService added successfully.")
                .build());
    }





}
