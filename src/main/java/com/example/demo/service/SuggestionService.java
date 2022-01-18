package com.example.demo.service;


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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;


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
        } else
            throw new SuggestionException("unqualify technician for the order");

    }


    @Transactional
    public void AcceptSuggestionByCust(Integer suggestionId) {
        Suggestion suggestion = loadById(suggestionId);
        suggestion.setStatus(SuggestionStatus.Accepted);
        super.update(suggestion);
    }

    public List<SuggestionOutputDto> sortSuggestionsBasedOnTechnicianPointDESC(Integer orderId, Pageable pageable) {
        return suggestionRepository.findAll((r, q, cb) -> {
            q.orderBy(cb.desc(r.get("technician").get("point")));
            return cb.equal(r.get("order").get("id"), orderId);
        }, pageable).stream().map(this::convertEntityToOutputDto).collect(Collectors.toList());

    }

    public List<SuggestionOutputDto> sortSuggestionsBasedOnSuggestedPriceASC(Integer orderId, Pageable pageable) {
        return suggestionRepository.findAll((r, q, cb) -> {
            q.orderBy(cb.asc(r.get("suggestion").get("suggestedPrice")));
            return cb.equal(r.get("order").get("id"), orderId);
        }, pageable).stream().map(this::convertEntityToOutputDto).collect(Collectors.toList());
    }

    public List<Suggestion> findSuggestionsByOrderId(Integer orderId) {
        return suggestionRepository.findSuggestionByOrderId(orderId);
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


}
