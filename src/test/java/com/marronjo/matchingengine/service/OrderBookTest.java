package com.marronjo.matchingengine.service;

import com.marronjo.matchingengine.domain.Order;
import com.marronjo.matchingengine.domain.Side;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Date;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration
public class OrderBookTest {

    private OrderBook orderBook;

    @BeforeEach
    public void init(){
        orderBook = new OrderBook("ABCD");
    }

    @Test
    public void addOrderTest(){
        Order order = new Order();
        order.setSide(Side.BUY);
        order.setPrice("101.34");
        order.setTimestamp(Date.from(Instant.now()));
        order.setClientId("client1");
        order.setQuantity("45");

        assertThat(orderBook.addOrder(order)).isNotNull();
    }
}
