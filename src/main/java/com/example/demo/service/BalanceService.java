package com.example.demo.service;


import com.example.demo.Exceptions.BalanceException;
import com.example.demo.Exceptions.CreditCardException;
import com.example.demo.dto.balance.InputBalanceDto;
import com.example.demo.dto.balance.OutputBalanceDto;
import com.example.demo.dto.creditcard.CreditCardInputDto;
import com.example.demo.model.*;
import com.example.demo.repository.BalanceRepository;
import com.example.demo.service.core.AbstractCRUD;

import com.example.demo.service.core.EntityConvertor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.DataOutput;
import java.sql.Date;

@Service
@RequiredArgsConstructor
public class BalanceService extends AbstractCRUD<Balance, Integer> implements
        EntityConvertor<InputBalanceDto, Balance, OutputBalanceDto> {

    private final BalanceRepository balanceRepository;
    private final CustomerService customerService;
    private final TechnicianService technicianService;
    private final SuggestionService suggestionService;
    private final CreditCardService cardService;


    @PostConstruct
    public void init() {
        setJpaRepository(balanceRepository);
    }


    @Transactional
    public void saveOrUpdate(Balance balance, Integer custID, Integer techID) {
        Customer customer = customerService.loadById(custID);
        Technician technician = technicianService.loadById(techID);

        balance.setCustomer(customer);
        balance.setTechnician(technician);

        super.save(balance);
    }

    @Transactional
    public void saveATransaction(InputBalanceDto inputBalanceDto) {
        Balance balance = convertInputToEntity(inputBalanceDto);
        Customer customer = balance.getCustomer();
        Technician technician = balance.getTechnician();
        Double cost = balance.getSuggestion().getSuggestedPrice();
        Integer suggestionId = balance.getSuggestion().getId();
        CreditCardInputDto creditCardInputDto = inputBalanceDto.getCreditCardInputDto();

        if(customerIsAbleToAffordTheSuggestion(creditCardInputDto,suggestionId,customer.getId())){
            Double custUpdatedCredit = customer.getCredit() - cost;
            customer.setCredit(custUpdatedCredit);
            customerService.update(customer);
            balance.setCustomerBalance(custUpdatedCredit);
            Double techUpdatedCredit = technician.getCredit() + cost;
            technician.setCredit(techUpdatedCredit);
            technicianService.update(technician);
            balance.setTechnicianBalance(techUpdatedCredit);
            super.update(balance);
        }else
            throw new BalanceException("Unsuccessfully Transaction");

    }


    public boolean customerIsAbleToAffordTheSuggestion(CreditCardInputDto creditCardInputDto,
                                                       Integer suggestionId, Integer customerId) {
        return (CreditIsSufficient(suggestionId, customerId) && CreditCardIsValid(creditCardInputDto));

    }


    public boolean CreditIsSufficient(Integer suggestionId, Integer customerId) {
        if (customerService.loadById(customerId).getCredit() >
                suggestionService.loadById(suggestionId).getSuggestedPrice()) {
            return true;
        } else
            throw new CreditCardException("Insufficient balance");

    }

    public Boolean CreditCardIsValid(CreditCardInputDto creditCardInputDto) {
        CreditCard creditCard = cardService.loadCreditCardByCustomerId(creditCardInputDto.getCustomerId());
        if (creditCard.getCardNumber().equals(creditCardInputDto.getCardNumber()) &&
                creditCard.getCVV2().equals(creditCardInputDto.getCVV2()) &&
                creditCard.getPassword().equals(creditCardInputDto.getPassword()) &&
                creditCard.getExpDate().equals(creditCardInputDto.getExpDate())) {
            return true;
        } else
            throw new CreditCardException("Mismatch creditcard");
    }


    @Override
    public Balance convertInputToEntity(InputBalanceDto inputBalanceDto) {
        return Balance.builder()
                .suggestion(suggestionService.loadById(inputBalanceDto.getSuggestionId()))
                .customerBalance(customerService.loadById(inputBalanceDto.getCustomerId()).getCredit())
                .customer(customerService.loadById(inputBalanceDto.getCustomerId()))
                .TechnicianBalance(technicianService.loadById(inputBalanceDto.getCustomerId()).getCredit())
                .technician(technicianService.loadById(inputBalanceDto.getCustomerId()))
                .transactionDate(new Date(System.currentTimeMillis()))
                .suggestion(suggestionService.loadById(inputBalanceDto.getSuggestionId()))
                .build();
    }

    @Override
    public OutputBalanceDto convertEntityToOutputDto(Balance balance) {
        return OutputBalanceDto.builder()
                .SuggestionId(balance.getSuggestion().getId())
                .transactionDate(new Date(System.currentTimeMillis()))
                .cost(balance.getSuggestion().getSuggestedPrice())
                .build();
    }


}
