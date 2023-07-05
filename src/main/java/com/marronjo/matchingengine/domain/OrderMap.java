package com.marronjo.matchingengine.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class OrderMap extends ConcurrentHashMap<Long, Order> {

    public List<Long> matchOrders(ArrayList<Long> idList, Long newTxId, Order newOrder){
        ArrayList<Long> ordersToMatch = new ArrayList<>();
        ArrayList<Long> ordersToRemove = new ArrayList<>();
        boolean breakLoop = false;
        for(Long id: idList){
            Order order = this.get(id);
            switch(checkOrderMatch(order, newOrder)){
                case NO_FILL -> {
                    breakLoop = true;
                }
                case EQUAL_FILL -> {
                    ordersToRemove.add(id);
                    this.remove(id);
                    this.remove(newTxId);
                    ordersToMatch.add(newTxId);
                }
                case EXISTING_ORDER_PARTIAL_FILL -> {
                    ordersToMatch.add(newTxId);
                    this.remove(newTxId);
                    order.setQuantity(order.getQuantity() - newOrder.getQuantity());
                    this.put(id, order);
                }
                case NEW_ORDER_PARTIAL_FILL -> {
                    ordersToRemove.add(id);
                    this.remove(id);
                    newOrder.setQuantity(newOrder.getQuantity() - order.getQuantity());
                    this.put(newTxId, newOrder);
                }
            }
            if(breakLoop) break;
        }
        ordersToRemove.forEach(idList::remove);
        return ordersToMatch;
    }

    private FillType checkOrderMatch(Order order, Order newOrder){
        if(order.getSide() == Side.BUY) {
            Integer matchType = checkMatch(order, newOrder);
            if(matchType == null) return FillType.NO_FILL;
            if(matchType == 0) return FillType.EQUAL_FILL;
            if(matchType > 0) return FillType.EXISTING_ORDER_PARTIAL_FILL;
            return FillType.NEW_ORDER_PARTIAL_FILL;
        }
        else {
            Integer matchType = checkMatch(newOrder, order);
            if(matchType == null) return FillType.NO_FILL;
            if(matchType == 0) return FillType.EQUAL_FILL;
            if(matchType > 0) return FillType.NEW_ORDER_PARTIAL_FILL;
            return FillType.EXISTING_ORDER_PARTIAL_FILL;
        }
    }

    private Integer checkMatch(Order buyOrder, Order sellOrder){
        if(buyOrder.getPrice().compareTo(sellOrder.getPrice()) >= 0){
            return buyOrder.getQuantity().compareTo(sellOrder.getQuantity());
        }
        return null;
    }
}
