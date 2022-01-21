package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long CardNumber;
    private Integer CVV2;
    private Date expDate;
    private String password;

    @OneToOne(cascade ={CascadeType.MERGE,CascadeType.PERSIST})
    private Customer customer;
}
