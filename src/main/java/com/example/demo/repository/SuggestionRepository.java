package com.example.demo.repository;


import com.example.demo.model.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SuggestionRepository extends JpaRepository<Suggestion, Integer> {

    List<Suggestion> findSuggestionByOrderIdOrderBySuggestedPriceAsc(Integer orderId);
    List<Suggestion> findSuggestionByOrderId(Integer orderId);

}
