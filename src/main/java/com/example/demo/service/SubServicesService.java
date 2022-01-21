package com.example.demo.service;

import com.example.demo.dto.subServices.RegisterSubServiceInputDto;
import com.example.demo.dto.subServices.SubServiceOutputDto;
import com.example.demo.dto.subServices.SubmitTechsToSubServicesInputDto;
import com.example.demo.model.SubServices;
import com.example.demo.model.Technician;
import com.example.demo.repository.SubServiceRepository;
import com.example.demo.service.core.AbstractCRUD;
import com.example.demo.service.core.EntityConvertor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubServicesService extends AbstractCRUD<SubServices, Integer> implements
        EntityConvertor<RegisterSubServiceInputDto, SubServices, SubServiceOutputDto> {

    private final SubServiceRepository subServiceRepository;
    private final MainServicesService mainServicesService;
    private final TechnicianService technicianService;

    @PostConstruct
    public void init() {
        setJpaRepository(subServiceRepository);
    }

    @Transactional
    public SubServiceOutputDto addSubService(RegisterSubServiceInputDto subServiceInputDto) {
        SubServices primarySubServices = convertInputToEntity(subServiceInputDto);
        return convertEntityToOutputDto(super.save(primarySubServices));
    }

    @Transactional(readOnly = true)
    public List<SubServiceOutputDto> loadAllSubServices() {
        List<SubServiceOutputDto> subServiceOutputDtos = new ArrayList<>();
        for (SubServices subServices : super.loadAll()) {
            subServiceOutputDtos.add(convertEntityToOutputDto(subServices));
        }
        return subServiceOutputDtos;
    }

    @Transactional
    public SubServiceOutputDto submitTechnicianToSubService(Integer subserviceId,Integer technicianId){
        List<Technician> technicianList = new ArrayList<>();
        Technician technician = technicianService.loadById(technicianId);
        technicianList.add(technician);
        SubServices subServices = super.loadById(subserviceId);
        subServices.setTechnicians(technicianList);
        super.update(subServices);
        return convertEntityToOutputDto(subServices);
    }

    @Override
    public SubServices convertInputToEntity(RegisterSubServiceInputDto subServiceInputDto) {
        return SubServices.builder()
                .subServicesName(subServiceInputDto.getName())
                .Description(subServiceInputDto.getDescription())
                .price(subServiceInputDto.getBasePrice())
                .mainServices(mainServicesService.loadById(subServiceInputDto.getMainServicesId()))
                .build();
    }

    @Override
    public SubServiceOutputDto convertEntityToOutputDto(SubServices subServices) {
        return SubServiceOutputDto.builder()
                .id(subServices.getId())
                .subServicesName(subServices.getSubServicesName())
                .price(subServices.getPrice())
                .build();

    }

}
