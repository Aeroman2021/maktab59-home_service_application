package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class Customer extends User {

    private Double credit;

    @OneToMany(mappedBy = "customer",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private List<Order> orders;

    @OneToMany(mappedBy = "customer",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private List<Balance> transactions;

    @OneToOne(mappedBy = "customer")
    private CreditCard creditCard;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Customer customer = (Customer) o;
        return getId() != null && Objects.equals(getId(), customer.getId());
    }



    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
