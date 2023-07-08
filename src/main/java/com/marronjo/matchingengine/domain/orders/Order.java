package com.marronjo.matchingengine.domain.orders;

import com.marronjo.matchingengine.domain.enums.Side;
import com.marronjo.matchingengine.domain.orders.request.OrderRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long orderId;
    private Float quantity;
    private Float price;
    private Side side;
    private Date timestamp;

    public void createOrderFromRequest(OrderRequest orderRequest){
        this.price = orderRequest.getPrice();
        this.quantity = orderRequest.getQuantity();
        this.side = orderRequest.getSide();
        this.timestamp = Date.from(Instant.now());
    }

    public String ToString(){
        return String.format("%d %f %f %s", orderId, price, quantity, timestamp.toString());
    }
}
