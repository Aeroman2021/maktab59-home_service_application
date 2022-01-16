package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "transaction_date")
    private Date transactionDate;

    @Column(name = "customer_current_blance")
    private Double customerBalance;

    @Column(name = "technecian_current_blance")
    private Double TechnecianBalance;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Technician technician;

}
