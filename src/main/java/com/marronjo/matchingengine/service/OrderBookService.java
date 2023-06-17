package com.marronjo.matchingengine.service;

import com.marronjo.matchingengine.domain.Order;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Service
public class OrderBookService {
    private final ConcurrentHashMap<String, OrderBook> orderBooks;

    public OrderBookService(){
        orderBooks = new ConcurrentHashMap<>();
        List.of("TSLA", "GOOGL", "AMZN", "AAPL").forEach(ticker -> orderBooks.put(ticker, new OrderBook(ticker)));
    }

    public Long addOrder(String ticker, Order order) throws Exception {
        OrderBook orderBook = orderBooks.get(ticker);
        if(orderBook == null) {
            throw new Exception("Unknown ticker symbol");
        }
        return orderBook.addOrder(order);
    }
}
