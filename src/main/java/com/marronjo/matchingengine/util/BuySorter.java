package com.marronjo.matchingengine.util;

import com.marronjo.matchingengine.domain.Order;

public class BuySorter implements ISorter {
    @Override
    public Boolean checkOrder(Order order, Order newOrder) {
        if(newOrder.getPrice().compareTo(order.getPrice()) > 0){
            return true;
        }
        return newOrder.getPrice().compareTo(order.getPrice()) == 0
                || newOrder.getTimestamp().compareTo(order.getTimestamp()) < 0;
    }
}
