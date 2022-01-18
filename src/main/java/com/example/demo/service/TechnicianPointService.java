package com.example.demo.service;

import com.example.demo.model.TechnicianPoint;
import com.example.demo.repository.TechnicianPointRepository;
import com.example.demo.service.core.AbstractCRUD;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class TechnicianPointService extends AbstractCRUD<TechnicianPoint,Integer> {

    private final TechnicianPointRepository technicianPointRepository;


    @PostConstruct
    public void init(){
        setJpaRepository(technicianPointRepository);
    }


}
