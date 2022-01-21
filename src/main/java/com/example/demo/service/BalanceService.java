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
import java.sql.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BalanceService extends AbstractCRUD<Balance, Integer> implements
        EntityConvertor<InputBalanceDto, Balance, OutputBalanceDto> {

    private final BalanceRepository balanceRepository;
    private final CustomerService customerService;
    private final TechnicianService technicianService;
    private final SuggestionService suggestionService;
    private final CreditCardService cardService;
    private final OrderService orderService;


    @PostConstruct
    public void init() {
        setJpaRepository(balanceRepository);
    }


    @Transactional
    public void intitialBalanceSave(Balance balance, Integer custID, Integer techID) {
        Customer customer = customerService.loadById(custID);
        Technician technician = technicianService.loadById(techID);

        balance.setCustomer(customer);
        balance.setTechnician(technician);

        super.save(balance);
    }

    @Transactional
    public OutputBalanceDto finalaizingAndCheckout(InputBalanceDto inputBalanceDto) {

        Balance balance = convertInputToEntity(inputBalanceDto);
        Customer customer = balance.getCustomer();
        System.out.println(customer.getFirstName());
        Technician technician = balance.getTechnician();
        System.out.println(technician.getFirstName());
        Double cost = balance.getSuggestion().getSuggestedPrice();
        System.out.println(cost);
        Integer suggestionId = balance.getSuggestion().getId();
        System.out.println(suggestionId);
        CreditCardInputDto creditCardInputDto = inputBalanceDto.getCreditCardInputDto();

        if (orderService.jobIsDone(inputBalanceDto.getOrderId()) &&
                customerIsAbleToAffordTheSuggestion(creditCardInputDto, suggestionId, customer.getId())) {
            Double custUpdatedCredit = customer.getCredit() - cost;
            customer.setCredit(custUpdatedCredit);
            customerService.update(customer);
            balance.setCustomerBalance(custUpdatedCredit);
            Double techUpdatedCredit = technician.getCredit() + (0.7 * cost);
            technician.setCredit(techUpdatedCredit);
            technicianService.update(technician);
            balance.setTechnicianBalance(techUpdatedCredit);
            super.save(balance);
            return convertEntityToOutputDto(balance);
        } else
            throw new BalanceException("Unsuccessfully Transaction");
    }


    public boolean customerIsAbleToAffordTheSuggestion(CreditCardInputDto creditCardInputDto,
                                                       Integer suggestionId, Integer customerId) {

        System.out.println("customerIsAbleToAffordTheSuggestion");
        return (CreditIsSufficient(suggestionId, customerId) && CreditCardIsValid(creditCardInputDto));

    }


    public boolean CreditIsSufficient(Integer suggestionId, Integer customerId) {
        System.out.println("CreditIsSufficient");
        if (customerService.loadById(customerId).getCredit() >
                suggestionService.loadById(suggestionId).getSuggestedPrice()) {
            return true;
        } else
            throw new CreditCardException("Insufficient balance");

    }

    public Boolean CreditCardIsValid(CreditCardInputDto creditCardInputDto) {
        System.out.println("CreditCardIsValid");
        CreditCard creditCard = cardService.loadCreditCardByCustomerId(creditCardInputDto.getCustomerId());
       if( (Objects.equals(creditCard.getCardNumber(), creditCardInputDto.getCardNumber()) &&
                Objects.equals(creditCard.getCVV2(), creditCardInputDto.getCvv2()) &&
                Objects.equals(creditCard.getPassword(), creditCardInputDto.getPassword()) &&
                creditCard.getExpDate() ==(creditCardInputDto.getExpDate())))
           return true;
       else
            throw new CreditCardException("Mismatch creditcard");
    }


    @Override
    public Balance convertInputToEntity(InputBalanceDto inputBalanceDto) {
        return Balance.builder()
                .suggestion(suggestionService.loadById(inputBalanceDto.getSuggestionId()))
                .customerBalance(customerService.loadById(inputBalanceDto.getCustomerId()).getCredit())
                .customer(customerService.loadById(inputBalanceDto.getCustomerId()))
                .TechnicianBalance(technicianService.loadById(inputBalanceDto.getTechnicianId()).getCredit())
                .technician(technicianService.loadById(inputBalanceDto.getTechnicianId()))
                .transactionDate(new Date(System.currentTimeMillis()))
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
