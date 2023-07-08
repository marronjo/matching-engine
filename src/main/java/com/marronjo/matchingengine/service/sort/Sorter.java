package com.marronjo.matchingengine.service.sort;

import com.marronjo.matchingengine.domain.orders.Order;

public interface Sorter {
    Boolean checkOrder(Order order, Order newOrder);
}
