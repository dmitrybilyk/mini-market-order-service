package com.minimarket.orders.orderservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimarket.orders.orderservice.controller.OrdersController;
import com.minimarket.orders.orderservice.dto.PriceResponse;
import com.minimarket.orders.orderservice.model.Order;
import com.minimarket.orders.orderservice.service.api.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(OrdersController.class)
class OrdersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private Order sampleOrder;

    private PriceResponse samplePriceResponse;

    @BeforeEach
    void setUp() {
        sampleOrder = Order.builder()
                .id(1L)
                .accountId("acc-123")
                .symbol("AAPL")
                .side(Order.Side.BUY)
                .quantity(10)
                .status(Order.Status.CREATED)
                .createdAt(OffsetDateTime.parse("2025-07-20T12:34:56Z"))
                .build();

        samplePriceResponse = PriceResponse.builder()
                .symbol("AAPL")
                .price(BigDecimal.valueOf(210.55))
                .build();
    }

    @Test
    void testGetOrderById() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(sampleOrder);

        mockMvc.perform(get("/orders/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleOrder.getId()))
                .andExpect(jsonPath("$.accountId").value(sampleOrder.getAccountId()))
                .andExpect(jsonPath("$.symbol").value(sampleOrder.getSymbol()))
                .andExpect(jsonPath("$.side").value(sampleOrder.getSide().toString()))
                .andExpect(jsonPath("$.quantity").value(sampleOrder.getQuantity()));
    }

    @Test
    void testGetOrdersForAccount() throws Exception {
        when(orderService.getOrdersByAccountId("acc-123")).thenReturn(List.of(sampleOrder));

        mockMvc.perform(get("/orders")
                        .param("accountId", "acc-123")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountId").value("acc-123"))
                .andExpect(jsonPath("$[0].symbol").value("AAPL"));
    }

    @Test
    void testSaveOrder() throws Exception {
        when(orderService.saveOrder(any(Order.class))).thenReturn(samplePriceResponse);

        String orderJson = objectMapper.writeValueAsString(sampleOrder);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol").value("AAPL"))
                .andExpect(jsonPath("$.price").value(210.55));
    }
}