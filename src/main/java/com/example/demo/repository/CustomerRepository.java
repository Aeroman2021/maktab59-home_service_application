package com.example.demo.repository;

import com.example.demo.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {


    Customer findCustomerByUsernameAndPassword(String username,String password);
    List<Customer> findCustomerByFirstName(String firstName);
    List<Customer> findCustomerByLastName(String lastName);
    Customer findCustomerByEmail(String Email);
    List<Customer> findCustomersByRegisterDateAfter(Date registerDate);
}
