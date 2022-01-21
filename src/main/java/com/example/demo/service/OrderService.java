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
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.example.demo.model.enums.OrderStatus.*;

@Service
@RequiredArgsConstructor
public class OrderService extends AbstractCRUD<Order, Integer> {

    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final SubServicesService subServicesService;
    private final TechnicianService technicianService;

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

    public List<Order> listOrdersBasedOnMainService(Integer mainServiceId) {
        Predicate<Order> ordersWithSpecifiedMainService = order ->
                order.getSubServices().getMainServices().getId().equals(mainServiceId);
        return super.loadAll().stream().filter(ordersWithSpecifiedMainService).collect(Collectors.toList());

    }


//    public List<Order> listRelatedOrdersForTechnicians(Integer technicianId) {
//        return orderRepository.listRelatedOrderForTechnicians(technicianId);
//    }

    public List<Technician> filterTechniciansBasedOnTheSubServiceId(Integer subServiceId) {
        return orderRepository.listTechniciansBasedOnTheSubServiceId(subServiceId);
    }

    public List<Order> findOrderByStatusIsPENDING_FOR_TECHNICIAN_SUGGESTION() {
        Predicate<Order> orderStatusIsPENDING_FOR_TECHNICIAN_SUGGESTION =
                order -> order.getStatus().equals(PENDING_FOR_TECHNICIAN_SUGGESTION);

        return super.loadAll()
                .stream()
                .filter(orderStatusIsPENDING_FOR_TECHNICIAN_SUGGESTION)
                .collect(Collectors.toList());
    }

    public List<Order> filterOrdersBaseOnRegisterDate(Date registerDate) {
        return orderRepository.findOrderByRegisterDate(registerDate);
    }

    public List<Order> findOrderByStatusIsPENDING_FOR_SELECTING_TECHNICIAN() {
        Predicate<Order> orderStatusIsPENDING_FOR_SELECTING_TECHNICIAN =
                order -> order.getStatus().equals(PENDING_FOR_SELECTING_TECHNICIAN);

        return super.loadAll()
                .stream()
                .filter(orderStatusIsPENDING_FOR_SELECTING_TECHNICIAN)
                .collect(Collectors.toList());
    }

    public List<Order> findOrderByStatusIsFINISHED_THE_PROCESS() {
        Predicate<Order> orderStatusIsFINISHED_THE_PROCESS =
                order -> order.getStatus().equals(FINISHED_THE_PROCESS);

        return super.loadAll()
                .stream()
                .filter(orderStatusIsFINISHED_THE_PROCESS)
                .collect(Collectors.toList());
    }


    public Boolean technicianIsQualifyForTheOrder(Integer technicianId, Integer orderId) {
        Integer subServiceId = super.loadById(orderId).getSubServices().getId();
        for (Technician technician : filterTechniciansBasedOnTheSubServiceId(subServiceId)) {
            if (Objects.equals(technician.getId(), technicianId))
                return true;
        }
        return false;
    }


    public List<OutputOrderInformationDto> loadOrdersByCustomerId(Integer customerId) {
        List<Order> ordersList = orderRepository.findOrderByCustomerId(customerId);
        List<OutputOrderInformationDto> orderInformationDtos = new ArrayList<>();
        if (!orderListIsEmpty(customerId)) {
            for (Order order : ordersList) {
                orderInformationDtos.add(convertEntityToOutputDto(order));
            }
            return orderInformationDtos;
        } else
            throw new OrderException("Empty order List.");
    }


    public boolean orderListIsEmpty(Integer cusromerId) {
        return orderRepository.findOrderByCustomerId(cusromerId).isEmpty();
    }


    public Integer numberOfSubmittedOrders(Integer customerId) {
        return orderRepository.findOrderByCustomerId(customerId).size();
    }

    @Transactional
    public OutputOrderInformationDto addOrderForCustomer(AddOrderForCustomerInputArgsDto inputArgsDto) {
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
            order.setStatus(PENDING_FOR_TECHNICIAN_SUGGESTION);
            super.save(order);
            return convertEntityToOutputDto(order);
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
                .orderStatus(order.getStatus())
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
        return super.loadById(orderId).getSuggestions() != null;
    }

    public boolean SuggestionIsAcceptedByCustomer(Integer orderId) {
        for (Suggestion suggestion : super.loadById(orderId).getSuggestions()) {
            if (suggestion.getStatus().equals(SuggestionStatus.Accepted))
                return true;
        }
        return false;
    }

    public Boolean updateOrderStatus_TO_PENDING_FOR_SELECTING_TECHNICIAN(Integer orderId) {
        if (OrderHasSuggestions(orderId)) {
            Order order = super.loadById(orderId);
            order.setStatus(PENDING_FOR_SELECTING_TECHNICIAN);
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
                    .orderStatus(PENDING_FOR_SELECTING_TECHNICIAN)
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

    public boolean jobIsDone(Integer orderId) {
        return super.loadById(orderId).getStatus().equals(FINISHED_THE_PROCESS);
    }

    public List<OutputOrderInformationDto> listRelatedOrdersForTechnicians(Integer technicianId) {
        List<OutputOrderInformationDto> orderInformationDtos = new ArrayList<>();
        Predicate<Order> selectedOrderForTechnicians = order ->
                order.getSubServices()
                        .getTechnicians()
                        .contains(technicianService.loadById(technicianId));
        List<Order> orders = super.loadAll().stream().filter(selectedOrderForTechnicians).toList();
        for (Order order : orders) {
            orderInformationDtos.add(convertEntityToOutputDto(order));
        }
        return orderInformationDtos;
    }


}