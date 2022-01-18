package com.example.demo.repository;


import com.example.demo.model.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SuggestionRepository extends JpaRepository<Suggestion, Integer> , JpaSpecificationExecutor<Suggestion> {

    List<Suggestion> findSuggestionByOrderIdOrderBySuggestedPriceAsc(Integer orderId);
    List<Suggestion> findSuggestionByOrderId(Integer orderId);

}
