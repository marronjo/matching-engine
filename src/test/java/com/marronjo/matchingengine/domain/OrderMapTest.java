package com.marronjo.matchingengine.domain;

import com.marronjo.matchingengine.domain.custom.IdList;
import com.marronjo.matchingengine.domain.custom.OrderMap;
import com.marronjo.matchingengine.domain.enums.Side;
import com.marronjo.matchingengine.domain.orders.Order;
import com.marronjo.matchingengine.service.sort.Sorter;
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
public class OrderMapTest {
    @InjectMocks
    private OrderMap orderMap;
    @InjectMocks
    private IdList<Sorter> buySideIds;
    @InjectMocks
    private IdList<Sorter> sellSideIds;

    private final Random random = new Random();

    @Test
    public void matchOrdersEqualExistingBuySideTest(){
        Order buySide = createRandomOrder(Side.BUY, 450F, 35F);
        Order sellSide = createRandomOrder(Side.SELL, 450F, 35F);

        Long buySideTxId = buySide.getOrderId();
        Long sellSideTxId = sellSide.getOrderId();

        orderMap.put(buySideTxId, buySide);
        orderMap.put(sellSideTxId, sellSide);

        buySideIds.add(buySideTxId);
        sellSideIds.add(sellSideTxId);

        List<Long> sellIdsToMatch = orderMap.matchOrders(buySideIds, sellSide);

        assertThat(sellIdsToMatch.size()).isEqualTo(1);
        assertThat(sellIdsToMatch.get(0)).isEqualTo(sellSideTxId);

        assertThat(orderMap).isEmpty();
    }

    @Test
    public void matchOrdersEqualExistingSellSideTest(){
        Order sellSide = createRandomOrder(Side.SELL, 450F, 35F);
        Order buySide = createRandomOrder(Side.BUY, 450F, 35F);

        Long buySideTxId = buySide.getOrderId();
        Long sellSideTxId = sellSide.getOrderId();

        orderMap.put(sellSideTxId, sellSide);
        orderMap.put(buySideTxId, buySide);

        sellSideIds.add(sellSideTxId);
        buySideIds.add(buySideTxId);

        List<Long> buyIdsToMatch = orderMap.matchOrders(sellSideIds, buySide);

        assertThat(buyIdsToMatch.size()).isEqualTo(1);
        assertThat(buyIdsToMatch.get(0)).isEqualTo(buySideTxId);

        assertThat(orderMap).isEmpty();
    }

    @Test
    public void partialMatchExistingBuySideTest(){
        Float buySideAmount = random.nextFloat(150, 200);
        Order buySide = createRandomOrder(Side.BUY, 96.75F, buySideAmount);

        Float sellSideAmount = random.nextFloat(100, 150);
        Order sellSide = createRandomOrder(Side.SELL, 96.34F, sellSideAmount);

        Long buySideTxId = buySide.getOrderId();
        Long sellSideTxId = sellSide.getOrderId();

        orderMap.put(sellSideTxId, sellSide);
        orderMap.put(buySideTxId, buySide);

        sellSideIds.add(sellSideTxId);
        buySideIds.add(buySideTxId);

        List<Long> sellIdsToMatch = orderMap.matchOrders(buySideIds, sellSide);

        assertThat(sellIdsToMatch.size()).isEqualTo(1);
        assertThat(sellIdsToMatch.get(0)).isEqualTo(sellSideTxId);

        assertThat(orderMap.get(buySideTxId).getQuantity()).isEqualTo(buySideAmount-sellSideAmount);
    }

    @Test
    public void partialMatchExistingSellSideTest(){
        Float sellSideAmount = random.nextFloat(900, 1300);
        Order sellSide = createRandomOrder(Side.SELL, 99.23F, sellSideAmount);

        Float buySideAmount = random.nextFloat(650, 900);
        Order buySide = createRandomOrder(Side.BUY, 100.75F, buySideAmount);

        Long buySideTxId = buySide.getOrderId();
        Long sellSideTxId = sellSide.getOrderId();

        orderMap.put(sellSideTxId, sellSide);
        orderMap.put(buySideTxId, buySide);

        sellSideIds.add(sellSideTxId);
        buySideIds.add(buySideTxId);

        List<Long> buyIdsToMatch = orderMap.matchOrders(sellSideIds, buySide);

        assertThat(buyIdsToMatch.size()).isEqualTo(1);
        assertThat(buyIdsToMatch.get(0)).isEqualTo(buySideTxId);

        assertThat(orderMap.get(sellSideTxId).getQuantity()).isEqualTo(sellSideAmount-buySideAmount);
    }

    @Test
    public void newOrderPartialMatchSellSideTest(){
        Float sellSideAmount = random.nextFloat(650, 900);
        Long sellSideTxId = 98346098L;
        Order sellSide = createRandomOrder(Side.SELL, 99.23F, sellSideAmount);

        Float buySideAmount = random.nextFloat(900, 1300);
        Long buySideTxId = 23476532L;
        Order buySide = createRandomOrder(Side.BUY, 100.75F, buySideAmount);

        orderMap.put(sellSideTxId, sellSide);
        orderMap.put(buySideTxId, buySide);

        sellSideIds.add(sellSideTxId);
        buySideIds.add(buySideTxId);

        List<Long> buyIdsToMatch = orderMap.matchOrders(sellSideIds, buySide);

        assertThat(buyIdsToMatch.isEmpty()).isTrue();

        assertThat(orderMap.get(buySideTxId).getQuantity()).isEqualTo(buySideAmount-sellSideAmount);
    }

    private Order createRandomOrder(Side side, Float price, Float quantity){
        return new Order(
                random.nextLong(),
                quantity,
                price,
                side,
                Date.from(Instant.now())
        );
    }
}
