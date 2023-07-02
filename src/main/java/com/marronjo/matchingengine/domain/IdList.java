package com.marronjo.matchingengine.domain;

import com.marronjo.matchingengine.service.match.Matcher;
import com.marronjo.matchingengine.service.sort.Sorter;
import org.jgroups.util.Tuple;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class IdList<S extends Sorter, M extends Matcher> extends ArrayList<Long> {
    private final Sorter sorter;
    private final Matcher matcher;

    public IdList(S sorter, M matcher){
        this.sorter = sorter;
        this.matcher = matcher;
    }

    public boolean sortIds(ConcurrentHashMap<Long, Order> orders, Long newTxId, Order newOrder) {
        int index = 0;
        for (Long id : this) {
            Order order = orders.get(id);
            if (sorter.checkOrder(order, newOrder)) {
                break;
            }
            index++;
        }
        this.add(index, newTxId);
        return index == 0;
    }

    public List<Long> matchOrders(ConcurrentHashMap<Long, Order> orders, Long newTxId, Order newOrder){
        ArrayList<Long> ordersToMatch = new ArrayList<>();
        ArrayList<Long> ordersToRemove = new ArrayList<>();
        for(Long id: this){
            Order order = orders.get(id);
            Tuple<Boolean, Integer> canMatch = matcher.checkMatch(order, newOrder);
            if(!canMatch.getVal1()) return ordersToMatch;
            boolean stillMatching = true;
            Integer matchType = canMatch.getVal2();
            while(stillMatching){
                if(matchType == 0) {
                    ordersToRemove.add(id);
                    orders.remove(id);
                    orders.remove(newTxId);
                    ordersToMatch.add(newTxId);
                    stillMatching = false;
                }
                else if (matchType < 0){
                    order.setQuantity(order.getQuantity() - newOrder.getQuantity());
                    orders.put(id, order);
                    orders.remove(newTxId);
                    ordersToMatch.add(newTxId);
                    stillMatching = false;
                }
                else {
                    ordersToRemove.add(id);
                    orders.remove(id);
                    order.setQuantity(newOrder.getQuantity() - order.getQuantity());
                    orders.put(newTxId, order);
                }
            }
        }
        ordersToRemove.forEach(this::remove);
        return ordersToMatch;
    }
}
