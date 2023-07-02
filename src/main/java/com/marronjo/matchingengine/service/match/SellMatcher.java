package com.marronjo.matchingengine.service.match;

import com.marronjo.matchingengine.domain.Order;
import org.jgroups.util.Tuple;

public class SellMatcher implements Matcher{
    //return > 0 : buy order quantity > top of book sell order quantity
    //return 0 : orders matched same quantity
    //return < 0 : buy order quantity < top of book sell order quantity
    @Override
    public Tuple<Boolean, Integer> checkMatch(Order sellOrder, Order buyOrder) {
        if(buyOrder.getPrice().compareTo(sellOrder.getPrice()) <= 0){
            return new Tuple<>(true,buyOrder.getQuantity().compareTo(sellOrder.getQuantity()));
        }
        return new Tuple<>(false,0);
    }
}
