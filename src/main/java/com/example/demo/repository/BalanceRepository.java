package com.example.demo.repository;


import com.example.demo.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<Balance, Integer> {

}
