package com.marronjo.matchingengine.service;

import com.marronjo.matchingengine.domain.Order;
import com.marronjo.matchingengine.domain.Side;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration
public class OrderBookTest {
    @InjectMocks
    private OrderBook orderBook;
    private final Random random = new Random();

    @Before
    public void init(){
        orderBook = new OrderBook("ABCD");
    }

    @Test
    public void addOrderTest(){
        Order order = new Order();
        order.setSide(Side.BUY);
        order.setPrice(101.34F);
        order.setTimestamp(Date.from(Instant.now()));
        order.setClientId("client1");
        order.setQuantity(23.76F);

        assertThat(orderBook.addOrder(order)).isNull();
    }

    @Test
    public void singleBuyOrderTest(){
        Order order = createSingleRandomOrder(Side.BUY);

        orderBook.addOrder(order);

        assertThat(orderBook.getOrders(Side.BUY).get(0)).isEqualTo(order);
        assertThat(orderBook.getOrders(Side.SELL)).isEmpty();
    }

    @Test
    public void singleSellOrderTest(){
        Order order = createSingleRandomOrder(Side.SELL);

        orderBook.addOrder(order);

        assertThat(orderBook.getOrders(Side.SELL).get(0)).isEqualTo(order);
        assertThat(orderBook.getOrders(Side.BUY)).isEmpty();
    }

    @Test
    public void addBuySellOrderTest(){
        Float buyPrice = 95F;
        Float sellPrice = 105F;
        Order buyOrder = createSingleRandomOrder(Side.BUY, buyPrice);
        Order sellOrder = createSingleRandomOrder(Side.SELL, sellPrice);

        Long buyTxId = orderBook.addOrder(buyOrder);
        Long sellTxId = orderBook.addOrder(sellOrder);

        List<Order> sellOrders = orderBook.getOrders(Side.SELL);
        assertThat(sellOrders.size()).isEqualTo(1);
        assertThat(sellOrders.get(0).getPrice()).isEqualTo(sellPrice);

        List<Order> buyOrders = orderBook.getOrders(Side.BUY);
        assertThat(buyOrders.size()).isEqualTo(1);
        assertThat(buyOrders.get(0).getPrice()).isEqualTo(buyPrice);
    }

    @Test
    public void add2MatchingOrdersSamePriceTest(){
        Float buyPrice = 100F;
        Float sellPrice = 100F;
        Float quantity = 50F;
        Order buyOrder = createSingleRandomOrder(Side.BUY, buyPrice, quantity);
        Order sellOrder = createSingleRandomOrder(Side.SELL, sellPrice, quantity);

        orderBook.addOrder(buyOrder);
        orderBook.addOrder(sellOrder);

        List<Order> sellOrders = orderBook.getOrders(Side.SELL);
        assertThat(sellOrders).isEmpty();

        List<Order> buyOrders = orderBook.getOrders(Side.BUY);
        assertThat(buyOrders).isEmpty();
    }

    private Order createSingleRandomOrder(Side side){
        return new Order(
                "client" + random.nextInt(),
                random.nextFloat(),
                random.nextFloat(),
                side,
                Date.from(Instant.now())
        );
    }

    private Order createSingleRandomOrder(Side side, Float price){
        return new Order(
                "client" + random.nextInt(),
                random.nextFloat(),
                price,
                side,
                Date.from(Instant.now())
        );
    }

    private Order createSingleRandomOrder(Side side, Float price, Float quantity){
        return new Order(
                "client" + random.nextInt(),
                quantity,
                price,
                side,
                Date.from(Instant.now())
        );
    }
}
