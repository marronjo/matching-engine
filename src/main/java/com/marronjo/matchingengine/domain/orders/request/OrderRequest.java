package com.marronjo.matchingengine.domain.orders.request;

import com.marronjo.matchingengine.domain.enums.OrderType;
import com.marronjo.matchingengine.domain.enums.Side;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private Float price;
    private Float quantity;
    private Side side;
    private OrderType orderType;
}
