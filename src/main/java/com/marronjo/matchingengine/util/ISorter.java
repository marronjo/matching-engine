package com.marronjo.matchingengine.util;

import com.marronjo.matchingengine.domain.Order;

public interface ISorter {
    Boolean checkOrder(Order order, Order newOrder);
}
