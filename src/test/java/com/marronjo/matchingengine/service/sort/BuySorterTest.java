package com.marronjo.matchingengine.service.sort;

import com.marronjo.matchingengine.domain.orders.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ContextConfiguration
public class BuySorterTest {

    @InjectMocks
    private BuySorter buySorter;

    @Test
    public void buyOrderPriceTest(){
        Order order1 = new Order();
        order1.setPrice(100F);

        Order order2 = new Order();
        order2.setPrice(105F);

        assertThat(buySorter.checkOrder(order1, order2)).isTrue();
    }

    @Test
    public void buyOrderTimestampTest(){
        Date d1 = Date.from(Instant.now());
        Date d2 = Date.from(Instant.now().plusSeconds(2L));

        Order order1 = new Order();
        order1.setPrice(100F);
        order1.setTimestamp(d1);

        Order order2 = new Order();
        order2.setPrice(100F);
        order2.setTimestamp(d2);

        assertThat(buySorter.checkOrder(order1, order2)).isFalse();
        assertThat(buySorter.checkOrder(order2, order1)).isTrue();
    }

    @Test
    public void buyOrderSameTimestampTest(){
        Date d1 = Date.from(Instant.now());

        Order order1 = new Order();
        order1.setPrice(100F);
        order1.setTimestamp(d1);

        Order order2 = new Order();
        order2.setPrice(100F);
        order2.setTimestamp(d1);

        assertThat(buySorter.checkOrder(order1, order2)).isFalse();
        assertThat(buySorter.checkOrder(order2, order1)).isFalse();
    }

    @Test
    public void buyOrderSameTimestampDiffPriceTest(){
        Date d1 = Date.from(Instant.now());

        Order order1 = new Order();
        order1.setPrice(100F);
        order1.setTimestamp(d1);

        Order order2 = new Order();
        order2.setPrice(95F);
        order2.setTimestamp(d1);

        assertThat(buySorter.checkOrder(order1, order2)).isFalse();
    }

    @Test
    public void buyOrderDiffTimestampDiffPriceTest(){
        Date d1 = Date.from(Instant.now());
        Date d2 = Date.from(Instant.now().plusSeconds(2L));

        Order order1 = new Order();
        order1.setPrice(100F);
        order1.setTimestamp(d1);

        Order order2 = new Order();
        order2.setPrice(95F);
        order2.setTimestamp(d2);

        assertThat(buySorter.checkOrder(order1, order2)).isFalse();
    }
}
