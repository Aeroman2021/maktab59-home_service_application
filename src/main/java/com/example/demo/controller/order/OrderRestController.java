package com.example.demo.controller.order;


import com.example.demo.controller.api.ResponseResult;
import com.example.demo.dto.order.AddOrderForCustomerInputArgsDto;
import com.example.demo.dto.order.OutputOrderInformationDto;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders/customers")
public class OrderRestController {

    private  final OrderService orderService;

    @PostMapping("/submitOrder")
    public ResponseEntity<ResponseResult<OutputOrderInformationDto>> addOrderForCustomer
            (@RequestBody AddOrderForCustomerInputArgsDto orderForCustomerInputArgsDto) {
        Order order = orderService.addOrderForCustomer(orderForCustomerInputArgsDto);
        OutputOrderInformationDto outputOrderInformationDto = orderService.convertEntityToOutputDto(order);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseResult.<OutputOrderInformationDto>builder()
                        .code(0)
                        .data(outputOrderInformationDto)
                        .message("order added for customer successfully.")
                        .build());
    }

    @PostMapping("/first_update/status")
    public ResponseEntity<ResponseResult<OutputOrderInformationDto>> firstUpdateOrderStatus(Integer orderId){
        OutputOrderInformationDto outputOrderInformationDto = orderService.convertOrderToFirstUpdatedOrder(orderId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseResult.<OutputOrderInformationDto>builder()
                        .code(0)
                        .data(outputOrderInformationDto)
                        .message("order status updated successfully.")
                        .build());
    }

    @PostMapping("/second_update/status")
    public ResponseEntity<ResponseResult<OutputOrderInformationDto>> secondUpdateOrderStatus(Integer orderId){
        OutputOrderInformationDto outputOrderInformationDto = orderService.convertOrderToSecondUpdatedOrder(orderId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseResult.<OutputOrderInformationDto>builder()
                        .code(0)
                        .data(outputOrderInformationDto)
                        .message("order status updated successfully.")
                        .build());
    }


}
