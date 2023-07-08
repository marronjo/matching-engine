package com.marronjo.matchingengine.controller;

import com.marronjo.matchingengine.domain.orders.request.OrderRequest;
import com.marronjo.matchingengine.domain.orders.response.OrderResponse;
import com.marronjo.matchingengine.service.OrderBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/orderbook")
public class OrderBookController {

    private final OrderBookService orderBookService;

    @PostMapping("send-order/{ticker}")
    public ResponseEntity<OrderResponse> sendOrder(@PathVariable String ticker, @RequestBody OrderRequest order){
        return ResponseEntity.status(HttpStatus.OK).body(orderBookService.submitOrder(ticker, order));
    }
}
