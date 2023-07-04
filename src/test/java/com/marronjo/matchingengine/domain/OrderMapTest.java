package com.marronjo.matchingengine.domain;

import com.marronjo.matchingengine.service.sort.Sorter;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Date;
import java.time.Instant;
import java.util.List;

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

    @Test
    public void matchOrdersEqualExistingBuySideTest(){
        Order buySide = createRandomOrder(Side.BUY, 450F, 35F);
        Long buySideTxId = 987342985L;
        Order sellSide = createRandomOrder(Side.SELL, 450F, 35F);
        Long sellSideTxId = 2349879584L;

        orderMap.put(buySideTxId, buySide);
        orderMap.put(sellSideTxId, sellSide);

        buySideIds.add(buySideTxId);
        sellSideIds.add(sellSideTxId);

        List<Long> sellIdsToMatch = orderMap.matchOrders(buySideIds, sellSideTxId, sellSide);

        assertThat(sellIdsToMatch.size()).isEqualTo(1);
        assertThat(sellIdsToMatch.get(0)).isEqualTo(sellSideTxId);

        assertThat(orderMap).isEmpty();
    }

    @Test
    public void matchOrdersEqualExistingSellSideTest(){
        Order sellSide = createRandomOrder(Side.SELL, 450F, 35F);
        Long sellSideTxId = 9273845398L;
        Order buySide = createRandomOrder(Side.BUY, 450F, 35F);
        Long buySideTxId = 330975732L;

        orderMap.put(sellSideTxId, sellSide);
        orderMap.put(buySideTxId, buySide);

        sellSideIds.add(sellSideTxId);
        buySideIds.add(buySideTxId);

        List<Long> buyIdsToMatch = orderMap.matchOrders(sellSideIds, buySideTxId, buySide);

        assertThat(buyIdsToMatch.size()).isEqualTo(1);
        assertThat(buyIdsToMatch.get(0)).isEqualTo(buySideTxId);

        assertThat(orderMap).isEmpty();
    }

    @Test
    public void partialMatchExistingBuySideTest(){
        Order buySide = createRandomOrder(Side.BUY, 96.75F, 150F);
        Long buySideTxId = 72304974L;
        Order sellSide = createRandomOrder(Side.SELL, 96.34F, 70F);
        Long sellSideTxId = 496983753L;

        orderMap.put(sellSideTxId, sellSide);
        orderMap.put(buySideTxId, buySide);

        sellSideIds.add(sellSideTxId);
        buySideIds.add(buySideTxId);

        List<Long> sellIdsToMatch = orderMap.matchOrders(buySideIds, sellSideTxId, sellSide);

        assertThat(sellIdsToMatch.size()).isEqualTo(1);
        assertThat(sellIdsToMatch.get(0)).isEqualTo(sellSideTxId);

        assertThat(orderMap.get(sellSideTxId).getQuantity()).isEqualTo(buySide.getQuantity()-sellSide.getQuantity());
    }

    private Order createRandomOrder(Side side, Float price, Float quantity){
        return new Order(
                "client",
                quantity,
                price,
                side,
                Date.from(Instant.now())
        );
    }
}
