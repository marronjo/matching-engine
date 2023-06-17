package com.marronjo.matchingengine.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Order {
    private String clientId;
    private String quantity;
    private String price;
    private Side side;
    private Date timestamp;
}
