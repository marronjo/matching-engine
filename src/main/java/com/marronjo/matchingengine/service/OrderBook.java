package com.marronjo.matchingengine.service;

import com.marronjo.matchingengine.domain.Order;
import com.marronjo.matchingengine.domain.Side;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class OrderBook {
    private final String ticker;
    private final Random random;
    private ConcurrentHashMap<Long, Order> buySide;
    private ConcurrentHashMap<Long, Order> sellSide;

    public OrderBook(String ticker){
        this.ticker = ticker;
        buySide = new ConcurrentHashMap<>();
        sellSide = new ConcurrentHashMap<>();

        random = new Random();
    }

    public Long addOrder(Order order){
        boolean newId = false;
        Long newTxId = 0L;
        while(!newId){
            newTxId = random.nextLong();
            if(order.getSide() == Side.BUY && buySide.get(newTxId) == null){
                buySide.put(newTxId, order);
                newId = true;
            }
            else if (order.getSide() == Side.SELL && sellSide.get(newTxId) == null){
                sellSide.put(newTxId, order);
                newId = true;
            }
        }
        return newTxId;
    }

    private void sort(){

    }

    private void checkMatch(){

    }
}
