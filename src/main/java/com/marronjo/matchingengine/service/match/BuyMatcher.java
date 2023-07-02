package com.marronjo.matchingengine.service.match;

import com.marronjo.matchingengine.domain.Order;
import org.jgroups.util.Tuple;


public class BuyMatcher implements Matcher{
    //return > 0 : sell order quantity > top of book buy order quantity
    //return 0 : orders matched same quantity
    //return < 0 : sell order quantity < top of book buy order quantity
    @Override
    public Tuple<Boolean, Integer> checkMatch(Order buyOrder, Order sellOrder) {
        if(sellOrder.getPrice().compareTo(buyOrder.getPrice()) <= 0){
            return new Tuple<>(true,sellOrder.getQuantity().compareTo(buyOrder.getQuantity()));
        }
        return new Tuple<>(false,0);
    }
}
