package com.marronjo.matchingengine.service;

import com.marronjo.matchingengine.domain.custom.IdList;
import com.marronjo.matchingengine.domain.orders.Order;
import com.marronjo.matchingengine.domain.custom.OrderMap;
import com.marronjo.matchingengine.domain.enums.Side;
import com.marronjo.matchingengine.service.sort.BuySorter;
import com.marronjo.matchingengine.service.sort.SellSorter;
import com.marronjo.matchingengine.service.sort.Sorter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public Long addOrder(Order order){
        boolean newId = false;
        Long newOrderId = 0L;
        while(!newId){
            newOrderId = random.nextLong(1000000, 9000000);
            if(orderMap.get(newOrderId) == null){
                order.setOrderId(newOrderId);
                orderMap.put(newOrderId, order);
                newId = true;
            }
        }
        if(sort(order)){
            checkMatch(order);
        }
        return newOrderId;
    }

    private List<Order> aggregateOrders(IdList<Sorter> idList){
        List<Order> aggregatedOrders = new ArrayList<>();
        idList.forEach(id -> aggregatedOrders.add(orderMap.get(id)));
        return aggregatedOrders;
    }

    private boolean sort(Order newOrder) {
        if(newOrder.getSide() == Side.BUY) return sortedBuyIds.sortIds(orderMap, newOrder);
        return sortedSellIds.sortIds(orderMap, newOrder);
    }

    private void checkMatch(Order order){
        for(Long id: getOrdersToMatch(order)){
            if(order.getSide() == Side.BUY) sortedBuyIds.remove(id);
            else sortedSellIds.remove(id);
        }
    }

    private List<Long> getOrdersToMatch(Order order){
        if(order.getSide() == Side.BUY) return orderMap.matchOrders(sortedSellIds, order);
        return orderMap.matchOrders(sortedBuyIds, order);
    }
}
