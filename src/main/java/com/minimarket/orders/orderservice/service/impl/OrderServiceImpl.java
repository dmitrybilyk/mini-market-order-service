package com.minimarket.orders.orderservice.service.impl;

import com.minimarket.orders.orderservice.model.Order;
import com.minimarket.orders.orderservice.service.api.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {


    @Override
    public Order getOrder() {
        return new Order();
    }
}
