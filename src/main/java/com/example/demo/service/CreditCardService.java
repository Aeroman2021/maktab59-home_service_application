package com.example.demo.service;

import com.example.demo.model.CreditCard;
import com.example.demo.repository.CreditCardRepository;
import com.example.demo.service.core.AbstractCRUD;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class CreditCardService extends AbstractCRUD<CreditCard,Integer> {
    private final CreditCardRepository creditCardRepository;

    @PostConstruct
    public void init(){
        setJpaRepository(creditCardRepository);
    }

    public CreditCard loadCreditCardByCustomerId(Integer customerId){
        return creditCardRepository.findCreditCardByCustomerId(customerId);
    }
}
