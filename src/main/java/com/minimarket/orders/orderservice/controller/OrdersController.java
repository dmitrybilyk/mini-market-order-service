package com.minimarket.orders.orderservice.controller;

import com.minimarket.orders.orderservice.model.Order;
import com.minimarket.orders.orderservice.service.api.OrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrdersController {

    private final OrderService orderService;

    public OrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping("/order")
    public Order getOrder() {
        return orderService.getOrder();
    }
}
