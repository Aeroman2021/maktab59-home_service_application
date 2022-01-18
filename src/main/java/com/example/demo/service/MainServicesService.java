package com.example.demo.service;

import com.example.demo.dto.mainServices.MainServiceOutputDto;
import com.example.demo.dto.mainServices.RegisterMainServiceInputDto;
import com.example.demo.model.MainServices;
import com.example.demo.repository.MainServicesRepository;
import com.example.demo.service.core.AbstractCRUD;
import com.example.demo.service.core.EntityConvertor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class MainServicesService extends AbstractCRUD<MainServices,Integer> implements
        EntityConvertor<RegisterMainServiceInputDto, MainServices, MainServiceOutputDto> {


    private final MainServicesRepository servicesRepository;

    public MainServiceOutputDto addMainService(RegisterMainServiceInputDto mainServiceInputDto){
        return convertEntityToOutputDto(super.save(convertInputToEntity(mainServiceInputDto)));
    }


    @PostConstruct
    public void init(){
        setJpaRepository(servicesRepository);
    }


    @Override
    public MainServices convertInputToEntity(RegisterMainServiceInputDto registerMainServiceInputDto) {
        return MainServices.builder()
                .mainServicesName(registerMainServiceInputDto.getName())
                .build();
    }

    @Override
    public MainServiceOutputDto convertEntityToOutputDto(MainServices mainServices) {
        return MainServiceOutputDto.builder()
                .id(mainServices.getId())
                .name(mainServices.getMainServicesName())
                .build();
    }
}
