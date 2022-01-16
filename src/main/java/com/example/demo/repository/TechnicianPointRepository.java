package com.example.demo.repository;

import com.example.demo.model.TechnicianPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface TechnicianPointRepository extends JpaRepository<TechnicianPoint,Integer> {
}
