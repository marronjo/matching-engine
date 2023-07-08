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
public class SellSorterTest {

    @InjectMocks
    private SellSorter sellSorter;

    @Test
    public void sellOrderPriceTest(){
        Order order1 = new Order();
        order1.setPrice(105F);

        Order order2 = new Order();
        order2.setPrice(100F);

        assertThat(sellSorter.checkOrder(order1, order2)).isTrue();
    }

    @Test
    public void sellOrderTimestampTest(){
        Date d1 = Date.from(Instant.now());
        Date d2 = Date.from(Instant.now().plusSeconds(2L));

        Order order1 = new Order();
        order1.setPrice(100F);
        order1.setTimestamp(d1);

        Order order2 = new Order();
        order2.setPrice(100F);
        order2.setTimestamp(d2);

        assertThat(sellSorter.checkOrder(order1, order2)).isFalse();
        assertThat(sellSorter.checkOrder(order2, order1)).isTrue();
    }

    @Test
    public void sellOrderSameTimestampTest(){
        Date d1 = Date.from(Instant.now());

        Order order1 = new Order();
        order1.setPrice(100F);
        order1.setTimestamp(d1);

        Order order2 = new Order();
        order2.setPrice(100F);
        order2.setTimestamp(d1);

        assertThat(sellSorter.checkOrder(order1, order2)).isFalse();
        assertThat(sellSorter.checkOrder(order2, order1)).isFalse();
    }

    @Test
    public void sellOrderSameTimestampDiffPriceTest(){
        Date d1 = Date.from(Instant.now());

        Order order1 = new Order();
        order1.setPrice(95F);
        order1.setTimestamp(d1);

        Order order2 = new Order();
        order2.setPrice(100F);
        order2.setTimestamp(d1);

        assertThat(sellSorter.checkOrder(order1, order2)).isFalse();
    }

    @Test
    public void sellOrderDiffTimestampDiffPriceTest(){
        Date d1 = Date.from(Instant.now());
        Date d2 = Date.from(Instant.now().plusSeconds(2L));

        Order order1 = new Order();
        order1.setPrice(95F);
        order1.setTimestamp(d1);

        Order order2 = new Order();
        order2.setPrice(100F);
        order2.setTimestamp(d2);

        assertThat(sellSorter.checkOrder(order1, order2)).isFalse();
    }
}

