package com.marronjo.matchingengine.domain;

import com.marronjo.matchingengine.service.sort.Sorter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class IdList<S extends Sorter> extends ArrayList<Long> {
    private final Sorter sorter;

    public IdList(S sorter){
        this.sorter = sorter;
    }

    public boolean sortIds(ConcurrentHashMap<Long, Order> orders, Order newOrder) {
        int index = 0;
        for (Long id : this) {
            Order order = orders.get(id);
            if (sorter.checkOrder(order, newOrder)) {
                break;
            }
            index++;
        }
        this.add(index, newOrder.getOrderId());
        return index == 0;
    }
}
