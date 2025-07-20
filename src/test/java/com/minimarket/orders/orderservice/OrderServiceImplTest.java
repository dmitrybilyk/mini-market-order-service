package com.minimarket.orders.orderservice;

import com.minimarket.orders.orderservice.dto.PriceResponse;
import com.minimarket.orders.orderservice.exception.RateLimitExceededException;
import com.minimarket.orders.orderservice.model.Order;
import com.minimarket.orders.orderservice.repository.OrderRepository;
import com.minimarket.orders.orderservice.ratelimiter.RateLimiterManager;
import com.minimarket.orders.orderservice.service.api.ExecutionService;
import com.minimarket.orders.orderservice.service.impl.OrderServiceImpl;
import com.minimarket.orders.orderservice.service.price.PriceService;
import io.github.resilience4j.ratelimiter.RateLimiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock private RateLimiterManager rateLimiterManager;
    @Mock private OrderRepository orderRepository;
    @Mock private PriceService priceService;
    @Mock private ExecutionService executionService;
    @Mock private RateLimiter rateLimiter;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveOrder_successful() {
        // given
        Order order = Order.builder()
                .accountId("acc-123")
                .symbol("AAPL")
                .side(Order.Side.BUY)
                .status(Order.Status.CREATED)
                .quantity(10)
                .createdAt(OffsetDateTime.now())
                .build();

        when(rateLimiterManager.getLimiter("acc-123")).thenReturn(rateLimiter);
        when(rateLimiter.acquirePermission(1)).thenReturn(true);
        when(priceService.getPrice(order)).thenReturn(new BigDecimal("210.5555555"));

        // when
        PriceResponse response = orderService.saveOrder(order);

        // then
        assertEquals("AAPL", response.getSymbol());
        assertEquals(new BigDecimal("210.555556"), response.getPrice());

        verify(orderRepository).save(order);
        verify(executionService).saveExecution(argThat(exec ->
                exec.getOrder().equals(order)
                        && exec.getPrice().equals(new BigDecimal("210.555556"))
                        && exec.getExecutedAt() != null
        ));
    }

    @Test
    void saveOrder_rateLimitExceeded() {
        Order order = Order.builder()
                .accountId("acc-123")
                .symbol("AAPL")
                .side(Order.Side.BUY)
                .quantity(10)
                .createdAt(OffsetDateTime.now())
                .build();

        when(rateLimiterManager.getLimiter("acc-123")).thenReturn(rateLimiter);
        when(rateLimiter.acquirePermission(1)).thenReturn(false);

        assertThrows(RateLimitExceededException.class, () -> orderService.saveOrder(order));

        verify(orderRepository, never()).save(any());
        verify(priceService, never()).getPrice(any());
    }
}
