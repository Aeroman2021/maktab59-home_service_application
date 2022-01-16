package com.example.demo.repository;


import com.example.demo.model.Technician;
import com.example.demo.model.enums.RegisterStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface TechnicianRepository extends JpaRepository<Technician, Integer> {

    List<Technician> findTechniciansByRegisterDate(Date registerDate);

    List<Technician> findTechniciansByStatus(RegisterStatus status);

    List<Technician> findTechnicianByFirstName(String firstName);

    List<Technician> findTechnicianByLastName(String lastName);

    Technician findTechnicianByEmail(String email);

    Technician findTechnicianByUsernameAndPassword(String username,String password);

    List<Technician> findTechnicianByAveragePointGreaterThan(Integer point);


    @Query(value = "select t.full_name," +
            " t.email" +
            " from final_project.sub_services_technicians st" +
            " join technicians t on" +
            " st.technicians_id = t.id" +
            " where st.sub_services_id=subServiceName", nativeQuery = true)
    List<Technician> findTechnicianBySubservicName(Integer subServiceName);
}
