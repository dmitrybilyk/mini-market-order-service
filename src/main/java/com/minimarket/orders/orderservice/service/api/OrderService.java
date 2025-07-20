package com.minimarket.orders.orderservice.service.api;

import com.minimarket.orders.orderservice.dto.PriceResponse;
import com.minimarket.orders.orderservice.model.Order;

import java.util.List;

public interface OrderService {

    PriceResponse saveOrder(Order order);

    Order getOrderById(Long id);

    List<Order> getOrdersByAccountId(String accountId);
}
