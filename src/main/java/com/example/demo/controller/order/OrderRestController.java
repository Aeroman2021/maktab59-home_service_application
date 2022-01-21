package com.example.demo.controller.order;


import com.example.demo.controller.api.ResponseResult;
import com.example.demo.dto.order.AddOrderForCustomerInputArgsDto;
import com.example.demo.dto.order.OutputOrderInformationDto;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderRestController {

    private  final OrderService orderService;

    @PostMapping("/submitOrder")
    public ResponseEntity<ResponseResult<OutputOrderInformationDto>> addOrderForCustomer
            (@RequestBody AddOrderForCustomerInputArgsDto orderForCustomerInputArgsDto) {
        OutputOrderInformationDto outputOrderInformationDto =
                orderService.addOrderForCustomer(orderForCustomerInputArgsDto);
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

    @GetMapping("/loadlist/relatedorders/{technicianId}")
    public ResponseEntity<ResponseResult<OutputOrderInformationDto>>
    listOrdersOfTheCustomer(@ PathVariable Integer technicianId){
        List<OutputOrderInformationDto> orderInformationDtos =
                orderService.listRelatedOrdersForTechnicians(technicianId);

        return ResponseEntity.ok(ResponseResult.<OutputOrderInformationDto>builder()
                .code(0)
                .dataList(orderInformationDtos)
                .message("The orderlist list loaded successfully")
                .build());
    }


    @GetMapping("/loadlist/submittedorders/{customerId}")
    public ResponseEntity<ResponseResult<OutputOrderInformationDto>> loadOrderByCustomerId(@PathVariable Integer customerId){
        List<OutputOrderInformationDto> orderInformationDtos =
                orderService.loadOrdersByCustomerId(customerId);

        return ResponseEntity.ok(ResponseResult.<OutputOrderInformationDto>builder()
                .code(0)
                .dataList(orderInformationDtos)
                .message("The orderlist list loaded successfully")
                .build());
    }


}
