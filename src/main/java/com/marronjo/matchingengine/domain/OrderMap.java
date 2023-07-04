package com.marronjo.matchingengine.domain;

import org.jgroups.util.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class OrderMap extends ConcurrentHashMap<Long, Order> {
    private Tuple<Boolean, Integer> checkMatch(Order order, Order newOrder){
        if(newOrder.getSide() == Side.BUY) return checkBuyMatch(newOrder, order);
        return checkSellMatch(newOrder, order);
    }

    private Tuple<Boolean, Integer> checkBuyMatch(Order buyOrder, Order sellOrder) {
        if(sellOrder.getPrice().compareTo(buyOrder.getPrice()) <= 0){
            return new Tuple<>(true,sellOrder.getQuantity().compareTo(buyOrder.getQuantity()));
        }
        return new Tuple<>(false,0);
    }

    private Tuple<Boolean, Integer> checkSellMatch(Order sellOrder, Order buyOrder) {
        if(buyOrder.getPrice().compareTo(sellOrder.getPrice()) >= 0){
            return new Tuple<>(true,buyOrder.getQuantity().compareTo(sellOrder.getQuantity()));
        }
        return new Tuple<>(false,0);
    }

    public List<Long> matchOrders(ArrayList<Long> idList, Long newTxId, Order newOrder){
        ArrayList<Long> ordersToMatch = new ArrayList<>();
        ArrayList<Long> ordersToRemove = new ArrayList<>();
        for(Long id: idList){
            Order order = this.get(id);
            Tuple<Boolean, Integer> canMatch = checkMatch(order, newOrder);
            if(!canMatch.getVal1()) return ordersToMatch;
            boolean stillMatching = true;
            Integer matchType = canMatch.getVal2();
            while(stillMatching){
                canMatch = checkMatch(order, newOrder);
                if(!canMatch.getVal1()) {
                    break;
                }
                if(matchType == 0) {
                    ordersToRemove.add(id);
                    this.remove(id);
                    this.remove(newTxId);
                    ordersToMatch.add(newTxId);
                    stillMatching = false;
                }
                else if (matchType < 0){
                    order.setQuantity(newOrder.getQuantity() - order.getQuantity());
                    this.put(id, order);
                    this.remove(newTxId);
                    ordersToMatch.add(newTxId);
                }
                else {
                    ordersToRemove.add(id);
                    this.remove(id);
                    order.setQuantity(order.getQuantity() - newOrder.getQuantity());
                    this.put(newTxId, order);
                    stillMatching = false;
                }
            }
        }
        ordersToRemove.forEach(idList::remove);
        return ordersToMatch;
    }
}
