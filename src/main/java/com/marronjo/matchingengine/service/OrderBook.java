package com.marronjo.matchingengine.service;

import com.marronjo.matchingengine.domain.IdList;
import com.marronjo.matchingengine.domain.Order;
import com.marronjo.matchingengine.domain.Side;
import com.marronjo.matchingengine.util.BuySorter;
import com.marronjo.matchingengine.util.SellSorter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class OrderBook {
    private final String ticker;
    private final ConcurrentHashMap<Long, Order> orders;
    private IdList<BuySorter> sortedBuyIds;
    private IdList<SellSorter> sortedSellIds;

    private final Random random;

    public OrderBook(String ticker){
        this.ticker = ticker;

        orders = new ConcurrentHashMap<>();
        sortedBuyIds = new IdList<>(new BuySorter());
        sortedSellIds = new IdList<>(new SellSorter());

        random = new Random();
    }

    public Long addOrder(Order order){
        boolean newId = false;
        Long newTxId = 0L;
        while(!newId){
            newTxId = random.nextLong();
            if(orders.get(newTxId) == null){
                if(order.getSide() == Side.BUY){
                    orders.put(newTxId, order);
                    newId = true;
                }
                else if (order.getSide() == Side.SELL){
                    orders.put(newTxId, order);
                    newId = true;
                }
            }
        }
        sort(order, newTxId);
        //check match
        return newTxId;
    }

    private void sort(Order newOrder, Long newTxId){
        if(newOrder.getSide() == Side.BUY){
            sortedBuyIds.sortIds(orders, newTxId, newOrder);
        }
        else if(newOrder.getSide() == Side.SELL){
            sortedBuyIds.sortIds(orders, newTxId, newOrder);
        }
    }

    private void checkMatch(Order order){

    }
}
