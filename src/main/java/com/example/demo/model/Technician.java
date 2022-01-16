package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "technicians")
public class Technician extends User {

    private Double credit;

    private Double averagePoint;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "technician")
    private List<TechnicianPoint> technicianPoints = new ArrayList<>();

    @Lob
    @Column(name = "profile_pic")
    private byte[] image;

    @OneToMany(mappedBy = "technician", cascade = CascadeType.ALL)
    private List<Suggestion> suggestions;

    @OneToMany(mappedBy = "technician", cascade = CascadeType.ALL)
    private List<Balance> transactions;

}
