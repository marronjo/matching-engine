package com.marronjo.matchingengine.service.sort;

import com.marronjo.matchingengine.domain.orders.Order;

public class BuySorter implements Sorter {
    @Override
    public Boolean checkOrder(Order order, Order newOrder) {
        if(newOrder.getPrice().compareTo(order.getPrice()) > 0){
            return true;
        }
        return newOrder.getPrice().compareTo(order.getPrice()) == 0
                || newOrder.getTimestamp().compareTo(order.getTimestamp()) < 0;
    }
}
