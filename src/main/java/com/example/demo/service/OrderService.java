package com.example.demo.service;

import com.example.demo.Exceptions.OrderException;
import com.example.demo.dto.address.SubmittedAddressDto;
import com.example.demo.dto.order.*;
import com.example.demo.model.*;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.model.enums.SuggestionStatus;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.core.AbstractCRUD;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService extends AbstractCRUD<Order, Integer> {

    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final SubServicesService subServicesService;
    private final SuggestionService suggestionService;
    private final OrderService orderService;

    @PostConstruct
    public void init() {
        setJpaRepository(orderRepository);
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public List<Order> getOrdersByRegisterDate(Date status) {
        return orderRepository.findOrderByRegisterDate(status);
    }

    public List<Order> listOrdersBySuggestedPrice() {
        return orderRepository.findAll(Sort.by(Sort.Direction.ASC, "suggestedPrice"));
    }

    public List<Order> listOrderBySubServiceId(Integer subServiceId) {
        return orderRepository.findOrderBySubServicesId(subServiceId);
    }

    public List<Order> listRelatedOrdersForTechnicians(Integer technicianId) {
        return orderRepository.listRelatedOrderForTechnicians(technicianId);
    }

    public List<Technician> filterTechniciansBasedOnTheSubServiceId(Integer subServiceId){
        return orderRepository.listTechniciansBasedOnTheSubServiceId(subServiceId);
    }

    public Boolean technicianIsQualifyForTheOrder(Integer technicianId, Integer orderId) {
        Integer subServiceId = orderService.loadById(orderId).getSubServices().getId();
        for (Technician technician : filterTechniciansBasedOnTheSubServiceId(subServiceId)) {
            if (Objects.equals(technician.getId(), technicianId))
                return true;
        }
        return false;
    }


    public List<OutputOrderInformationDto> loadOrdersByCustomerId(Integer customerId) {
        List<Order> ordersList = orderRepository.findOrderByCustomerId(customerId);
        List<OutputOrderInformationDto> orderInformationDtos = new ArrayList<>();
        if (ordersList.size() != 0) {
            {
                for (Order order : ordersList) {
                    orderInformationDtos.add(convertEntityToOutputDto(order));
                }
            }
            return orderInformationDtos;
        } else
            throw new OrderException("Empty order List.");
    }

    @Transactional
    public Order addOrderForCustomer(AddOrderForCustomerInputArgsDto inputArgsDto) {
        Integer subServiceId = inputArgsDto.getSubServiceId();
        Integer customerId = inputArgsDto.getCustomerId();
        InputOrderInformationDto submittedOrderDto = inputArgsDto.getSubmittedOrderDto();
        SubmittedAddressDto submittedAddressDto = inputArgsDto.getSubmittedAddressDto();
        Order order = convertInputToEntity(submittedOrderDto, submittedAddressDto);
        Customer customer = customerService.loadById(customerId);
        SubServices subServices = subServicesService.loadById(subServiceId);

        if (!orderIsDuplicate(subServiceId, customerId)) {
            order.setCustomer(customer);
            order.setSubServices(subServices);
            order.setStatus(OrderStatus.PENDING_FOR_TECHNICIAN_SUGGESTION);
            return super.save(order);
        } else {
            throw new OrderException("Unable to submit Order!");
        }
    }

    public Address convertInputAddressToAddressEntity(SubmittedAddressDto submittedAddressDto) {
        return Address.builder()
                .province(submittedAddressDto.getProvince())
                .city(submittedAddressDto.getCity())
                .street(submittedAddressDto.getStreet())
                .Alley(submittedAddressDto.getAlley())
                .houseNumber(submittedAddressDto.getHomeNumber())
                .postalCode(submittedAddressDto.getPostalCode())
                .build();
    }

    public Order convertInputToEntity(InputOrderInformationDto inputOrderInformationDto,
                                      SubmittedAddressDto submittedAddressDto) {
        return Order.builder()
                .suggestedPrice(inputOrderInformationDto.getSuggestedPrice())
                .description(inputOrderInformationDto.getDescription())
                .startDate(inputOrderInformationDto.getStartDate())
                .registerDate(new Date(System.currentTimeMillis()))
                .address(convertInputAddressToAddressEntity(submittedAddressDto))
                .build();
    }


    public OutputOrderInformationDto convertEntityToOutputDto(Order order) {
        return OutputOrderInformationDto.builder()
                .orderId(order.getId())
                .customerId(order.getCustomer().getId())
                .serviceId(order.getSubServices().getId())
                .orderStatus(OrderStatus.PENDING_FOR_TECHNICIAN_SUGGESTION)
                .build();
    }

    public boolean orderIsDuplicate(Integer customerId, Integer subServiceId) {
        for (Order order : super.loadAll()) {
            if (order.getSubServices().getId().equals(subServiceId) &&
                    order.getCustomer().getId().equals(customerId))
                return true;
        }
        return false;
    }

    public boolean OrderHasSuggestions(Integer orderId) {
        return suggestionService.findSuggestionsByOrderId(orderId) != null;
        // orderservice
    }

    public boolean SuggestionIsAcceptedByCustomer(Integer orderId) {
        for (Suggestion suggestion : suggestionService.findSuggestionsByOrderId(orderId)) {
            if (suggestion.getStatus().equals(SuggestionStatus.Accepted))
                return true;
        }
        return false;

        //orderservice
    }

    public Boolean updateOrderStatus_TO_PENDING_FOR_SELECTING_TECHNICIAN(Integer orderId) {
        if (OrderHasSuggestions(orderId)) {
            Order order = super.loadById(orderId);
            order.setStatus(OrderStatus.PENDING_FOR_SELECTING_TECHNICIAN);
            super.update(order);
            return true;
        }
        return false;
    }

    public Boolean updateOrderStatusTO_Waiting_FOR_TECHNICIAN_TO_COME_TO_PLACE(Integer orderId) {
        if (SuggestionIsAcceptedByCustomer(orderId)) {
            Order order = super.loadById(orderId);
            order.setStatus(OrderStatus.PENDING_FOR_COMING_TO_PLACE);
            super.update(order);
            return true;
        }
        return false;
    }


    public OutputOrderInformationDto convertOrderToFirstUpdatedOrder(Integer orderId) {
        Order order = super.loadById(orderId);
        if (updateOrderStatus_TO_PENDING_FOR_SELECTING_TECHNICIAN(orderId)) {
            return OutputOrderInformationDto.builder()
                    .orderId(orderId)
                    .customerId(order.getCustomer().getId())
                    .serviceId(order.getSubServices().getId())
                    .orderStatus(OrderStatus.PENDING_FOR_SELECTING_TECHNICIAN)
                    .build();
        } else
            return null;
    }

    public OutputOrderInformationDto convertOrderToSecondUpdatedOrder(Integer orderId) {
        Order order = super.loadById(orderId);
        if (updateOrderStatusTO_Waiting_FOR_TECHNICIAN_TO_COME_TO_PLACE(orderId)) {
            return OutputOrderInformationDto.builder()
                    .orderId(orderId)
                    .customerId(order.getCustomer().getId())
                    .serviceId(order.getSubServices().getId())
                    .orderStatus(OrderStatus.PENDING_FOR_COMING_TO_PLACE)
                    .build();
        } else
            return null;
    }

    public List<OutputOrderInformationDto> printRelatedOrdersForTechnicians(Integer technicianId){
        List<OutputOrderInformationDto> orderInformationDtos = new ArrayList<>();
        for (Order order : listRelatedOrdersForTechnicians(technicianId)) {
            OutputOrderInformationDto outputOrderInformationDto = convertEntityToOutputDto(order);
            orderInformationDtos.add(outputOrderInformationDto);
        }
        return orderInformationDtos;
    }


}