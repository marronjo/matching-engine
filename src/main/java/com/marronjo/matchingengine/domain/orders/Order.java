package com.marronjo.matchingengine.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public String ToString(){
        return String.format("%d %f %f %s", orderId, price, quantity, timestamp.toString());
    }
}
