package com.example.demo.model;


import com.example.demo.model.enums.SubServicesName;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "title", "assistance_id" }) })
public class SubServices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private SubServicesName subServicesName;

    @OneToMany(mappedBy = "subServices")
    private List<Order> orders;

    @ManyToOne
    private MainServices mainServices;

    private String Description;

    @Column(name = "base_price")
    private Double price;


    @ManyToMany
    private List<Technician> technicians;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubServices)) return false;
        SubServices that = (SubServices) o;
        return getSubServicesName() == that.getSubServicesName() &&
                getPrice().equals(that.getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSubServicesName(), getPrice());
    }
}
