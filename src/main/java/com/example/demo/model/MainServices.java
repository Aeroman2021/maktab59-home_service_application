package com.example.demo.model;

import com.example.demo.model.enums.MainServicesName;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MainServices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private MainServicesName mainServicesName;

    @OneToMany(mappedBy = "mainServices")
    private Set<SubServices> subServices;

}
