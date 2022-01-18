package com.example.demo.service;

import com.example.demo.model.Admin;
import com.example.demo.repository.AdminRepository;
import com.example.demo.service.core.AbstractCRUD;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class AdminService extends AbstractCRUD<Admin,Integer> {

    private final AdminRepository adminRepository;

    @PostConstruct
    public void init(){
        setJpaRepository(adminRepository);
    }

}
