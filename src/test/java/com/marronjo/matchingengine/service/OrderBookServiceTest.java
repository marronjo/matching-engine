package com.marronjo.matchingengine.service;

import com.marronjo.matchingengine.domain.enums.OrderType;
import com.marronjo.matchingengine.domain.enums.Side;
import com.marronjo.matchingengine.domain.orders.Order;
import com.marronjo.matchingengine.domain.orders.request.OrderRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ContextConfiguration
public class OrderBookServiceTest {
    @InjectMocks
    private OrderBookService orderBookService;

    @Test
    public void addOrderTest(){
        Order order = new Order();
        assertThat(orderBookService.addOrder("TSLA", order)).isNotNull();
    }

    @Test
    public void addOrderTickerDNETest(){
        Order order = new Order();
        assertThat(orderBookService.addOrder("UNKOWN", order)).isNull();
    }

    @Test
    public void submitOrderTest(){
        OrderRequest o = new OrderRequest(100F, 50F, Side.BUY, OrderType.LIMIT);
        assertThat(orderBookService.submitOrder("TSLA", o).getSuccess()).isTrue();
    }

    @Test
    public void submitOrderTickerDNETest(){
        OrderRequest o = new OrderRequest(100F, 50F, Side.BUY, OrderType.LIMIT);
        assertThat(orderBookService.submitOrder("UNKNOWN", o).getSuccess()).isFalse();
    }

    @Test
    public void aggregateOrdersTest(){
        assertThat(orderBookService.getOrderBook("TSLA")).isNotNull();
    }

    @Test
    public void aggregateOrdersTickerDNETest(){
        assertThat(orderBookService.getOrderBook("UNKNOWN")).isNull();
    }
}
