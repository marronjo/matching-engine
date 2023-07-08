package com.marronjo.matchingengine.controller;

import com.marronjo.matchingengine.domain.orders.request.OrderRequest;
import com.marronjo.matchingengine.service.OrderBookService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ContextConfiguration
public class OrderBookControllerTest {

    @InjectMocks
    private OrderBookController orderBookController;

    @Mock
    private OrderBookService orderBookService;

    @Test
    public void orderBookControllerTest(){
        assertThat(orderBookController.sendOrder("TSLA", new OrderRequest())).isNotNull();
    }
}
