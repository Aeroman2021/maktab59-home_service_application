package com.example.demo.controller.users;


import com.example.demo.controller.api.ResponseResult;
import com.example.demo.dto.Comment.InputCommentDto;
import com.example.demo.dto.Comment.OutputCommentDto;
import com.example.demo.dto.order.OutputOrderInformationDto;
import com.example.demo.dto.subServices.SubServiceOutputDto;
import com.example.demo.dto.suggestion.SuggestionOutputDto;
import com.example.demo.dto.technicianpoint.OutPutPointDto;
import com.example.demo.dto.users.core.UserOutputDto;
import com.example.demo.dto.users.customer.input.RegisterCustomerInputDto;
import com.example.demo.model.TechnicianPoint;
import com.example.demo.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerRestController {

    private final CustomerService customerService;
    private final SubServicesService subServicesService;
    private final OrderService orderService;
    private final CommentService commentService;
    private final SuggestionService suggestionService;

    @PostMapping("/addCustomer")
    public ResponseEntity<ResponseResult<UserOutputDto>> saveCustomer
            (@RequestBody RegisterCustomerInputDto customerInputDto) {
        UserOutputDto userOutputDto = customerService.addCustomer(customerInputDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseResult.<UserOutputDto>builder()
                        .code(0)
                        .data(userOutputDto)
                        .message("customer added successfully.")
                        .build());
    }

    @PostMapping("/comments")
    public ResponseEntity<ResponseResult<OutputCommentDto>> addComment(@RequestBody InputCommentDto inputCommentDto) {
        OutputCommentDto outputCommentDto = commentService.addCommentForOrder(inputCommentDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseResult.<OutputCommentDto>builder()
                        .code(0)
                        .data(outputCommentDto)
                        .message("comment added to the order successfully.")
                        .build());
    }

    @PostMapping("/submit/point")
    public ResponseEntity<ResponseResult<OutPutPointDto>> submitPointToTechnicians(@RequestBody TechnicianPoint technicianPoint) {
        OutPutPointDto outPutPointDto = customerService.setPointForTheTechnicians(technicianPoint);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseResult.<OutPutPointDto>builder()
                        .code(0)
                        .data(outPutPointDto)
                        .message("The point submitted to the technician successfully.")
                        .build());
    }

    @GetMapping("/List/subservices")
    public ResponseEntity<ResponseResult<SubServiceOutputDto>> saveSubService() {
        List<SubServiceOutputDto> subServiceOutputDtos = subServicesService.loadAllSubServices();
        return ResponseEntity.ok(ResponseResult.<SubServiceOutputDto>builder()
                .code(0)
                .dataList(subServiceOutputDtos)
                .message(" The subservices List loaded successfully.")
                .build());
    }

    @GetMapping("/List/orders/listOfOrders")
    public ResponseEntity<ResponseResult<SubServiceOutputDto>> loadOrderList() {
        List<SubServiceOutputDto> subServiceOutputDtos = subServicesService.loadAllSubServices();
        return ResponseEntity.ok(ResponseResult.<SubServiceOutputDto>builder()
                .code(0)
                .dataList(subServiceOutputDtos)
                .message(" The subservices List loaded successfully.")
                .build());
    }

    @GetMapping("/List/orders/listOfOrders/{customerId}")
    public ResponseEntity<ResponseResult<OutputOrderInformationDto>> addOrderForCustomer
            (@PathVariable Integer customerId) {
        List<OutputOrderInformationDto> orderInformationDtos = orderService.loadOrdersByCustomerId(customerId);
        return ResponseEntity.ok(ResponseResult.<OutputOrderInformationDto>builder()
                .code(0)
                .dataList(orderInformationDtos)
                .message("the order List loaded successfully.")
                .build());
    }

    @GetMapping("List/Suggestions/filterByPoint/{orderId}")
    public ResponseEntity<ResponseResult<SuggestionOutputDto>> filterSuggestionBasedOnTechnicianByPoint
            (@PathVariable Integer orderId, Pageable pageable) {
        List<SuggestionOutputDto> suggestionOutputDtos =
                suggestionService.sortSuggestionsBasedOnTechnicianPointDESC(orderId, pageable);

        return ResponseEntity.ok(ResponseResult.<SuggestionOutputDto>builder()
                .code(0)
                .dataList(suggestionOutputDtos)
                .message("the suggestion List loaded successfully.")
                .build());
    }

    @GetMapping("List/Suggestions/filterByPrice/{orderId}")
    public ResponseEntity<ResponseResult<SuggestionOutputDto>> filterSuggestionBasedOnSuggestedPrice
            (@PathVariable Integer orderId, Pageable pageable) {
        List<SuggestionOutputDto> suggestionOutputDtos =
                suggestionService.sortSuggestionsBasedOnSuggestedPriceASC(orderId, pageable);
        return ResponseEntity.ok(ResponseResult.<SuggestionOutputDto>builder()
                .code(0)
                .dataList(suggestionOutputDtos)
                .message("the suggestion List loaded successfully.")
                .build());

    }


}
