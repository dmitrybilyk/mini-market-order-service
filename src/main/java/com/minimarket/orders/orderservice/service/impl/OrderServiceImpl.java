package com.minimarket.orders.orderservice.service.impl;

import com.minimarket.orders.orderservice.dto.PriceResponse;
import com.minimarket.orders.orderservice.repository.OrderRepository;
import com.minimarket.orders.orderservice.exception.OrderNotFoundException;
import com.minimarket.orders.orderservice.exception.RateLimitExceededException;
import com.minimarket.orders.orderservice.model.Execution;
import com.minimarket.orders.orderservice.model.Order;
import com.minimarket.orders.orderservice.ratelimiter.RateLimiterManager;
import com.minimarket.orders.orderservice.service.api.ExecutionService;
import com.minimarket.orders.orderservice.service.api.OrderService;
import com.minimarket.orders.orderservice.service.price.PriceService;
import io.github.resilience4j.ratelimiter.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Implementation of {@link OrderService} for managing orders in the minimarket system.
 * Handles order creation, retrieval, and rate limiting for account-specific orders.
 */
@Service
public class OrderServiceImpl implements OrderService {

    private static final String RATE_LIMIT_EXCEEDED_MESSAGE = "Rate limit exceeded for account: %s";
    private static final int RATE_LIMIT_PERMITS = 1;

    private final RateLimiterManager rateLimiterManager;
    private final OrderRepository orderRepository;
    private final PriceService priceService;
    private final ExecutionService executionService;

    /**
     * Constructs an {@code OrderServiceImpl} with required dependencies.
     *
     * @param rateLimiterManager manages rate limiting for accounts
     * @param orderRepository handles persistence of orders
     * @param priceService provides pricing information for orders
     * @param executionService manages execution records
     */
    public OrderServiceImpl(RateLimiterManager rateLimiterManager, OrderRepository orderRepository,
                            PriceService priceService, ExecutionService executionService) {
        this.rateLimiterManager = rateLimiterManager;
        this.orderRepository = orderRepository;
        this.priceService = priceService;
        this.executionService = executionService;
    }

    /**
     * Saves an order, applies rate limiting, retrieves price, and records execution.
     *
     * @param order the order to save
     * @return a {@link PriceResponse} with the order's price and symbol
     * @throws RateLimitExceededException if the account exceeds its rate limit
     * @throws IllegalArgumentException if the order or required fields are null
     */
    @Override
    @Transactional
    public PriceResponse saveOrder(Order order) {
        validateOrder(order);
        checkRateLimit(order.getAccountId());

        BigDecimal price = calculatePrice(order);
        PriceResponse priceResponse = buildPriceResponse(order, price);
        persistOrderAndExecution(order, price);

        return priceResponse;
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param id the ID of the order
     * @return the {@link Order} associated with the ID
     * @throws OrderNotFoundException if no order is found
     * @throws IllegalArgumentException if the ID is null
     */
    @Override
    public Order getOrderById(Long id) {
        Objects.requireNonNull(id, "Order ID cannot be null");
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    /**
     * Retrieves all orders for a given account ID.
     *
     * @param accountId the ID of the account
     * @return a list of {@link Order} objects for the account
     * @throws IllegalArgumentException if the account ID is null
     */
    @Override
    public List<Order> getOrdersByAccountId(String accountId) {
        Objects.requireNonNull(accountId, "Account ID cannot be null");
        return orderRepository.findByAccountId(accountId);
    }

    private void validateOrder(Order order) {
        Objects.requireNonNull(order, "Order cannot be null");
        Objects.requireNonNull(order.getAccountId(), "Account ID cannot be null");
        Objects.requireNonNull(order.getSymbol(), "Symbol cannot be null");
    }

    private void checkRateLimit(String accountId) {
        RateLimiter limiter = rateLimiterManager.getLimiter(accountId);
        if (!limiter.acquirePermission(RATE_LIMIT_PERMITS)) {
            throw new RateLimitExceededException(String.format(RATE_LIMIT_EXCEEDED_MESSAGE, accountId));
        }
    }

    private BigDecimal calculatePrice(Order order) {
        return priceService.getPrice(order).setScale(6, RoundingMode.HALF_UP);
    }

    private PriceResponse buildPriceResponse(Order order, BigDecimal price) {
        return PriceResponse.builder()
                .symbol(order.getSymbol())
                .price(price)
                .build();
    }

    private void persistOrderAndExecution(Order order, BigDecimal price) {
        orderRepository.save(order);
        executionService.saveExecution(Execution.builder()
                .order(order)
                .price(price)
                .executedAt(OffsetDateTime.now())
                .build());
    }
}