package com.marronjo.matchingengine.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String clientId;
    private Float quantity;
    private Float price;
    private Side side;
    private Date timestamp;
}
