package com.marronjo.matchingengine.service.sort;

import com.marronjo.matchingengine.domain.Order;

public interface Sorter {
    Boolean checkOrder(Order order, Order newOrder);
}
