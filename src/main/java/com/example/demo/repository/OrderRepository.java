package com.example.demo.repository;

import com.example.demo.model.Order;
import com.example.demo.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByStatus(OrderStatus status);
    List<Order> findOrderByRegisterDate(Date date);
    List<Order> findOrderByCustomerId(Integer customerId);

}
