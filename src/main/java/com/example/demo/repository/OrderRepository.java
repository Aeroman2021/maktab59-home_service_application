package com.example.demo.repository;

import com.example.demo.model.Order;
import com.example.demo.model.Technician;
import com.example.demo.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByStatus(OrderStatus status);

    List<Order> findOrderByRegisterDate(Date date);

    List<Order> findOrderByCustomerId(Integer customerId);

    List<Order> findOrderBySubServicesId(Integer subServiceId);

    @Query(value = " use home_service_application;" +
            " select * " +
            " from technicians t " +
            " join sub_services_technicians sst on" +
            " t.id = sst.technicians_id" +
            " join orders o on " +
            " sst.sub_services_id = o.sub_services_id" +
            " where o.sub_services_id = ?1 ", nativeQuery = true)
    List<Technician> listTechniciansBasedOnTheSubServiceId(Integer subServiceId);


    @Query(value = " use home_service_application; " +
            " select * " +
            " from orders o " +
            " join sub_services_technicians sst on " +
            " o.sub_services_id = sst.sub_services_id " +
            " join technicians t on " +
            " sst.technicians_id = t.id " +
            " where t.id = ?1", nativeQuery = true)
    List<Order> listRelatedOrderForTechnicians(Integer technicianId);

}
