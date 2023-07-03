package com.marronjo.matchingengine.service;

import com.marronjo.matchingengine.domain.IdList;
import com.marronjo.matchingengine.domain.Order;
import com.marronjo.matchingengine.domain.OrderMap;
import com.marronjo.matchingengine.domain.Side;
import com.marronjo.matchingengine.service.sort.BuySorter;
import com.marronjo.matchingengine.service.sort.SellSorter;
import com.marronjo.matchingengine.service.sort.Sorter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class OrderBook {
    private final String ticker;
    private final OrderMap orderMap;
    private final IdList<Sorter> sortedBuyIds;
    private final IdList<Sorter> sortedSellIds;

    private final Random random;

    public OrderBook(String ticker){
        this.ticker = ticker;
        orderMap = new OrderMap();
        sortedBuyIds = new IdList<>(new BuySorter());
        sortedSellIds = new IdList<>(new SellSorter());

        random = new Random();
    }

    public List<Order> getOrders(Side side){
        if(side == Side.BUY) return aggregateOrders(sortedBuyIds);
        return aggregateOrders(sortedSellIds);
    }

    private List<Order> aggregateOrders(IdList<Sorter> idList){
        List<Order> aggregatedOrders = new ArrayList<>();
        idList.forEach(id -> aggregatedOrders.add(orderMap.get(id)));
        return aggregatedOrders;
    }

    public Long addOrder(Order order){
        boolean newId = false;
        Long newTxId = 0L;
        while(!newId){
            newTxId = random.nextLong();
            if(orderMap.get(newTxId) == null){
                if(order.getSide() == Side.BUY){
                    orderMap.put(newTxId, order);
                    newId = true;
                }
                else if (order.getSide() == Side.SELL){
                    orderMap.put(newTxId, order);
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
        if(newOrder.getSide() == Side.BUY) return sortedBuyIds.sortIds(orderMap, newTxId, newOrder);
        return sortedSellIds.sortIds(orderMap, newTxId, newOrder);
    }

    private void checkMatch(Order order, Long newTxId){
        for(Long id: getOrdersToMatch(order, newTxId)){
            if(order.getSide() == Side.BUY) sortedBuyIds.remove(id);
            else sortedSellIds.remove(id);
        }
    }

    private List<Long> getOrdersToMatch(Order order, Long newTxId){
        if(order.getSide() == Side.BUY) return orderMap.matchOrders(sortedSellIds, newTxId, order);
        return orderMap.matchOrders(sortedBuyIds, newTxId, order);
    }
}
