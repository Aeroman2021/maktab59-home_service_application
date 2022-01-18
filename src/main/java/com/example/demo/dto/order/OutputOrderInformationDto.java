package com.example.demo.dto.order;


import com.example.demo.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutputOrderInformationDto {

    private Integer orderId;
    private Integer customerId;
    private Integer serviceId;
    private OrderStatus orderStatus = OrderStatus.PENDING_FOR_TECHNICIAN_SUGGESTION;


}
