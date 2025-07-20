package com.minimarket.orders.orderservice.controller;

import com.minimarket.orders.orderservice.dto.PriceResponse;
import com.minimarket.orders.orderservice.model.Order;
import com.minimarket.orders.orderservice.service.api.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrdersController {

    private final OrderService orderService;

    public OrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping("/orders/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @RequestMapping("/orders")
    public ResponseEntity<List<Order>> getOrdersForAccount(@RequestParam String accountId) {
        return ResponseEntity.ok(orderService.getOrdersByAccountId(accountId));
    }

    @PostMapping("/orders")
    public ResponseEntity<PriceResponse> saveOrder(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.saveOrder(order));
    }

}
