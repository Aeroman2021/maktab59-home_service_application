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
public class OutputOrderSecondUpdateStatusDto {
    private Integer orderId;
    private Integer customerId;
    private Integer serviceId;
    private OrderStatus orderStatus = OrderStatus.PENDING_FOR_COMING_TO_PLACE;
}
