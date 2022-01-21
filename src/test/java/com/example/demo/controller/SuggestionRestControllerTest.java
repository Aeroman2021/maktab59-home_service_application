package com.example.demo.controller;


import com.example.demo.controller.suggestion.SuggestionRestController;
import com.example.demo.controller.users.TechnicianRestController;
import com.example.demo.dto.suggestion.SuggestionOutputDto;
import com.example.demo.service.SuggestionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(SuggestionRestController.class)
@ActiveProfiles("test")
public class SuggestionRestControllerTest extends AbstractRestControllerTest {

    @MockBean
    private  SuggestionService suggestionService;

    @Test
    public void loadCompletedOrdersByTechs_isOK() throws Exception {
        List<SuggestionOutputDto> resultList =new ArrayList<>();
        SuggestionOutputDto suggestionOutputDto = SuggestionOutputDto.builder()
                .technicianId(1)
                .orderId(1)
                .suggestionId(1)
                .build();

        resultList.add(suggestionOutputDto);
        Mockito.when(suggestionService.listCompletedOrdersByTechnicians(1)).thenReturn(resultList);

        mvc.perform(get("/suggestions/finishedorders/technicians/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.dataList[0].suggestionId").value(resultList.get(0).getSuggestionId()))
                .andExpect(jsonPath("$.dataList[0].technicianId").value(resultList.get(0).getTechnicianId()))
                .andExpect(jsonPath("$.dataList[0].orderId").value(resultList.get(0).getOrderId()));
    }


}
