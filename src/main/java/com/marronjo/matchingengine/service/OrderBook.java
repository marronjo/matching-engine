package com.marronjo.matchingengine.service;

import com.marronjo.matchingengine.domain.IdList;
import com.marronjo.matchingengine.domain.Order;
import com.marronjo.matchingengine.domain.Side;
import com.marronjo.matchingengine.service.match.BuyMatcher;
import com.marronjo.matchingengine.service.match.Matcher;
import com.marronjo.matchingengine.service.match.SellMatcher;
import com.marronjo.matchingengine.service.sort.BuySorter;
import com.marronjo.matchingengine.service.sort.SellSorter;
import com.marronjo.matchingengine.service.sort.Sorter;
import org.jgroups.util.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class OrderBook {
    private final String ticker;
    private final ConcurrentHashMap<Long, Order> orders;
    private IdList<Sorter, Matcher> sortedBuyIds;
    private IdList<Sorter, Matcher> sortedSellIds;

    private final Random random;

    public OrderBook(String ticker){
        this.ticker = ticker;

        orders = new ConcurrentHashMap<>();
        sortedBuyIds = new IdList<>(new BuySorter(), new BuyMatcher());
        sortedSellIds = new IdList<>(new SellSorter(), new SellMatcher());

        random = new Random();
    }

    public List<Order> getOrders(Side side){
        if(side == Side.BUY) return aggregateOrders(sortedBuyIds);
        return aggregateOrders(sortedSellIds);
    }

    private List<Order> aggregateOrders(IdList<Sorter, Matcher> idList){
        List<Order> aggregatedOrders = new ArrayList<>();
        idList.forEach(id -> aggregatedOrders.add(orders.get(id)));
        return aggregatedOrders;
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
        if(sort(order, newTxId)){
            checkMatch(order, newTxId);
        }
        return newTxId;
    }

    private boolean sort(Order newOrder, Long newTxId) {
        if(newOrder.getSide() == Side.BUY) return sortedBuyIds.sortIds(orders, newTxId, newOrder);
        return sortedSellIds.sortIds(orders, newTxId, newOrder);
    }

    private void checkMatch(Order order, Long newTxId){
        for(Long id: getOrdersToMatch(order, newTxId)){
            if(order.getSide() == Side.BUY) sortedBuyIds.remove(id);
            else sortedSellIds.remove(id);
        }
    }

    private List<Long> getOrdersToMatch(Order order, Long newTxId){
        if(order.getSide() == Side.BUY) return sortedSellIds.matchOrders(orders, newTxId, order);
        return sortedBuyIds.matchOrders(orders, newTxId, order);
    }
}
