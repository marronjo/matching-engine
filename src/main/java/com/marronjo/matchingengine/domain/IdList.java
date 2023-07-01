package com.marronjo.matchingengine.domain;

import com.marronjo.matchingengine.util.ISorter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class IdList<T extends ISorter> extends ArrayList<Long> {
    private final ISorter sorter;
    public IdList(T sorter){
        this.sorter = sorter;
    }
    public void sortIds(ConcurrentHashMap<Long, Order> orders, Long newTxId, Order newOrder) {
        int index = 0;
        for (Long id : this) {
            Order order = orders.get(id);
            if (sorter.checkOrder(order, newOrder)) {
                break;
            }
            index++;
        }
        this.add(index, newTxId);
    }
}
