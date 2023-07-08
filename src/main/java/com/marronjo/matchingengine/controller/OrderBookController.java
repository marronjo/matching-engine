package com.marronjo.matchingengine.controller;

import com.marronjo.matchingengine.domain.orders.request.OrderRequest;
import com.marronjo.matchingengine.domain.orders.response.OrderResponse;
import com.marronjo.matchingengine.service.OrderBookService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.marronjo.matchingengine.domain.constant.Constants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ORDERBOOK_URL)
public class OrderBookController {

    private final OrderBookService orderBookService;

    @PostMapping(SEND_ORDER_URL)
    @ApiOperation(value = SEND_ORDER_VALUE, notes = SEND_ORDER_NOTES)
    public ResponseEntity<OrderResponse> sendOrder(@PathVariable String ticker, @RequestBody OrderRequest order){
        return ResponseEntity.status(HttpStatus.OK).body(orderBookService.submitOrder(ticker, order));
    }
}
