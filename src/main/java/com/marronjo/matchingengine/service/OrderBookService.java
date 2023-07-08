package com.marronjo.matchingengine.service;

import com.marronjo.matchingengine.domain.orders.AggregatedOrderbook;
import com.marronjo.matchingengine.domain.orders.Order;
import com.marronjo.matchingengine.domain.enums.Side;
import com.marronjo.matchingengine.domain.orders.request.OrderRequest;
import com.marronjo.matchingengine.domain.orders.response.OrderResponse;
import lombok.extern.log4j.Log4j2;
import org.jgroups.util.Tuple;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Service
public class OrderBookService {
    private final ConcurrentHashMap<String, OrderBook> orderBooks;

    public OrderBookService(){
        orderBooks = new ConcurrentHashMap<>();
        List.of("TSLA").forEach(ticker -> orderBooks.put(ticker, new OrderBook(ticker)));
    }

    public OrderResponse submitOrder(String ticker, OrderRequest orderRequest){
        Order newOrder =  new Order();
        newOrder.createOrderFromRequest(orderRequest);
        Long newOrderId = addOrder(ticker, newOrder);

        if(newOrderId == null) return new OrderResponse(false, null);
        return new OrderResponse(true, newOrderId);
    }

    public Long addOrder(String ticker, Order order) {
        OrderBook orderBook = orderBooks.get(ticker);
        if(orderBook == null) {
            log.error("Error fetching orderBook");
            return null;
        }
        return orderBook.addOrder(order);
    }

    public AggregatedOrderbook getOrderBook(String ticker){
        OrderBook orderBook = orderBooks.get(ticker);
        return new AggregatedOrderbook(ticker, orderBook.getOrders(Side.BUY), orderBook.getOrders(Side.SELL));
    }
}
