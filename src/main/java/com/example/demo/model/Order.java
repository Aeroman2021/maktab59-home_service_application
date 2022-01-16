package com.example.demo.model;

import com.example.demo.model.enums.OrderStatus;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String description;

    @ManyToOne
    private SubServices subServices;

    @ManyToOne(cascade = CascadeType.ALL)
    private Customer customer;

    @Column(name = "suggested_price")
    private double suggestedPrice;

    @Column(name = "register_date")
    private Date registerDate;

    @Column(name = "date_of_starting_job")
    private Date startDate;

    @OneToMany(mappedBy = "order",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Suggestion> suggestions;

    @Embedded
    private Address address;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Double.compare(order.getSuggestedPrice(), getSuggestedPrice()) == 0 &&
                getStatus() == order.getStatus() &&
                this.getSubServices().equals(order.getSubServices()) &&
                getCustomer().equals(order.getCustomer()) &&
                getRegisterDate().equals(order.getRegisterDate()) &&
                getStartDate().equals(order.getStartDate()) &&
                getAddress().equals(order.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatus(), this.getSubServices(), getCustomer(), getSuggestedPrice(),
                getRegisterDate(), getStartDate(), getAddress());
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", subServices=" + subServices +
                ", customer=" + customer +
                ", suggestedPrice=" + suggestedPrice +
                ", registerDate=" + registerDate +
                ", startDate=" + startDate +
                ", suggestions=" + suggestions +
                ", address=" + address +
                '}';
    }
}
