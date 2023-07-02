package com.marronjo.matchingengine.service.match;

import com.marronjo.matchingengine.domain.Order;
import org.jgroups.util.Tuple;

public interface Matcher {
    Tuple<Boolean, Integer> checkMatch(Order buyOrder, Order sellOrder);
}
