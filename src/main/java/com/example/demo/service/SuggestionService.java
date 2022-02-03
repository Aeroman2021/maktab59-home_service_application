package com.example.demo.service;


import com.example.demo.Exceptions.OrderException;
import com.example.demo.Exceptions.SuggestionException;
import com.example.demo.dto.suggestion.SuggestionInputDto;
import com.example.demo.dto.suggestion.SuggestionOutputDto;
import com.example.demo.dto.suggestion.SuggestionPrimaryInformationDto;
import com.example.demo.model.Order;
import com.example.demo.model.Suggestion;
import com.example.demo.model.Technician;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.model.enums.SuggestionStatus;
import com.example.demo.repository.SuggestionRepository;
import com.example.demo.service.core.AbstractCRUD;
import com.example.demo.service.core.EntityConvertor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SuggestionService extends AbstractCRUD<Suggestion, Integer> implements
        EntityConvertor<SuggestionInputDto, Suggestion, SuggestionOutputDto> {

    private final SuggestionRepository suggestionRepository;
    private final OrderService orderService;
    private final TechnicianService technicianService;

    @PostConstruct
    public void init() {
        setJpaRepository(suggestionRepository);
    }

    @Transactional
    public void saveOrUpdate(Suggestion suggestion, Integer orderId, Integer technicianId) {
        Order order = orderService.loadById(orderId);
        Technician technician = technicianService.loadById(technicianId);
        suggestion.setOrder(order);
        suggestion.setTechnician(technician);
        suggestion.setStatus(SuggestionStatus.Accepted);
        super.save(suggestion);
    }


    @Transactional
    public SuggestionOutputDto submitSuggestionByTechnicians(SuggestionInputDto suggestionInputDto) {
        Order order = orderService.loadById(suggestionInputDto.getOrderId());
        Technician technician = technicianService.loadById(suggestionInputDto.getTechnicianId());
        Suggestion suggestion = convertInputToEntity(suggestionInputDto);

        if (orderService.technicianIsQualifyForTheOrder(technician.getId(), order.getId())) {
            suggestion.setTechnician(technician);
            suggestion.setOrder(order);
            order.setStatus(OrderStatus.PENDING_FOR_SELECTING_TECHNICIAN);
            return convertEntityToOutputDto(super.save(suggestion));
        } else{
            log.error("the technician with thw id {} is not qualify for the order with id {} "
                    ,technician.getId(),order.getId() );
            throw new SuggestionException("unqualify technician for the order");
        }
    }


    @Transactional
    public void AcceptSuggestionByCust(Integer suggestionId) {
        Suggestion suggestion = loadById(suggestionId);
        suggestion.setStatus(SuggestionStatus.Accepted);
        super.update(suggestion);
    }

    public List<SuggestionOutputDto> sortSuggestionsBasedOnTechnicianPointDESC(Integer orderId, Pageable pageable) {
        if(!orderIsExist(orderId))
            throw new OrderException("Order not exist");

        return suggestionRepository.findAll((r, q, cb) -> {
            q.orderBy(cb.desc(r.get("technician").get("averagePoint")));
            return cb.equal(r.get("order").get("id"), orderId);
        }, pageable).stream().map(this::convertEntityToOutputDto).collect(Collectors.toList());

    }

    public List<SuggestionOutputDto> sortSuggestionsBasedOnSuggestedPriceASC(Integer orderId, Pageable pageable) {

        if(!orderIsExist(orderId))
            throw new OrderException("Order not exist");

        return suggestionRepository.findAll((r, q, cb) -> {
            q.orderBy(cb.asc(r.get("suggestedPrice")));
            return cb.equal(r.get("order").get("id"), orderId);
        }, pageable).stream().map(this::convertEntityToOutputDto).collect(Collectors.toList());
    }

    public List<Suggestion> findSuggestionsByOrderId(Integer orderId) {
        return suggestionRepository.findSuggestionByOrderId(orderId);
    }

    public boolean orderIsExist(Integer orderId){
        return orderService.loadById(orderId) != null;
    }


    @Override
    public Suggestion convertInputToEntity(SuggestionInputDto suggestionInputDto) {
        SuggestionPrimaryInformationDto suggestionPrimaryInformationDto =
                suggestionInputDto.getSuggestionPrimaryInformationDto();
        return Suggestion.builder()
                .startHour(suggestionPrimaryInformationDto.getStartHour())
                .registerSuggestion(suggestionPrimaryInformationDto.getRegisterSuggestion())
                .suggestedPrice(suggestionPrimaryInformationDto.getSuggestedPrice())
                .workDuration(suggestionPrimaryInformationDto.getWorkDuration())
                .status(SuggestionStatus.Pending)
                .build();
    }

    @Override
    public SuggestionOutputDto convertEntityToOutputDto(Suggestion suggestion) {
        return SuggestionOutputDto.builder()
                .suggestionId(suggestion.getId())
                .orderId(suggestion.getOrder().getId())
                .technicianId(suggestion.getTechnician().getId())
                .build();
    }

    public List<SuggestionOutputDto> listCompletedOrdersByTechnicians(Integer technicianId) {
        List<Suggestion> suggestions = suggestionRepository.findSuggestionsByTechnicianId(technicianId);
        List<SuggestionOutputDto> result = new ArrayList<>();
        Predicate<Suggestion> finishedJobPredicate =
                suggestion -> suggestion.getOrder().getStatus().equals(OrderStatus.FINISHED_THE_PROCESS);

        for (Suggestion suggestion : suggestions.stream().filter(finishedJobPredicate).toList()) {
            result.add(convertEntityToOutputDto(suggestion));
        }

        if(result.isEmpty()){
            log.error("No completed order found for the the technician with id {} ", technicianId);
            throw new OrderException("No completed order found for the the technician");
        }

        return result;
    }

    public Integer numberOfSuggestions(Integer technicianId){
        return suggestionRepository.findSuggestionsByTechnicianId(technicianId).size();
    }



}
