package com.marronjo.matchingengine.service;

import com.marronjo.matchingengine.domain.orders.AggregatedOrderbook;
import com.marronjo.matchingengine.domain.orders.Order;
import com.marronjo.matchingengine.domain.enums.Side;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Random;

@Log4j2
@Service
@RequiredArgsConstructor
public class AutoTrader {

    private final OrderBookService orderBookService;
    private boolean keepRunning = false;
    private final Random random = new Random();

    public void toggleAutoTrader(String ticker){
        if(keepRunning) keepRunning = false;
        else{
            keepRunning = true;
            new Thread(() -> startAutoTrader(ticker)).start();
        }
    }

    public boolean getAutoTraderStatus(){
        return keepRunning;
    }

    private void startAutoTrader(String ticker){
        while(keepRunning){
            try{
                orderBookService.addOrder(ticker, createRandomOrder());
                printOrderBook(orderBookService.getOrderBook(ticker));
                Thread.sleep(1000L);
            }catch (Exception e){
                log.error("Error encountered running autoTrader");
            }
        }
    }

    private void printOrderBook(AggregatedOrderbook orderBook){
        log.info("---- {} Orderbook ----", orderBook.getTicker());
        log.info("BUY");
        for(Order buyOrder: orderBook.getBuyOrders()){
            log.info("\t\t"+buyOrder.ToString());
        }
        log.info("SELL");
        for(Order sellOrder: orderBook.getSellOrders()){
            log.info("\t\t"+sellOrder.ToString());
        }
        log.info("\n");
    }

    private Order createRandomOrder(){
        Side side = random.nextInt() % 2 == 0 ? Side.BUY : Side.SELL;
        return new Order(
                random.nextLong(),
                random.nextFloat(10,250),
                random.nextFloat(95,105),
                side,
                Date.from(Instant.now())
        );
    }
}
