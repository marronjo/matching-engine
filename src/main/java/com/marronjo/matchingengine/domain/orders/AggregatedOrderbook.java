package com.marronjo.matchingengine.domain.orders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggregatedOrderbook {
    private String ticker;
    private List<Order> buyOrders;
    private List<Order> sellOrders;
}
