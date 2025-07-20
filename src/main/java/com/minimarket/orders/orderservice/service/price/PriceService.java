package com.minimarket.orders.orderservice.service.price;

import com.minimarket.orders.orderservice.exception.IllegalQuantityException;
import com.minimarket.orders.orderservice.model.Order;
import com.minimarket.orders.orderservice.dto.PriceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

/**
 * Service class responsible for retrieving price information for orders.
 * This service interacts with an external price API using a WebClient to fetch
 * the price for a given order based on its symbol.
 */
@Service
@Slf4j
public class PriceService {

    private final WebClient webClient;

    /**
     * Constructs a {@code PriceService} instance with a configured WebClient.
     * The WebClient is initialized with a base URL derived from the provided
     * WireMock host and port configuration properties.
     *
     * @param wiremockHost the host address of the WireMock server, injected from configuration
     * @param wiremockPort the port of the WireMock server, injected from configuration
     */
    public PriceService(@Value("${wiremock.host}") String wiremockHost,
                        @Value("${wiremock.port}") String wiremockPort) {
        log.info("wiremock host: {}", wiremockHost);
        webClient = WebClient.builder()
                .baseUrl("http://" + wiremockHost + ":" + wiremockPort)
                .build();
    }

    /**
     * Retrieves the price for a given order by making a request to the external price API.
     * The price is fetched based on the order's symbol, and the quantity is validated
     * to ensure it is positive.
     *
     * @param order the {@link Order} object containing the symbol and quantity
     * @return the price as a {@link BigDecimal} for the specified order's symbol
     * @throws IllegalQuantityException if the order's quantity is less than or equal to zero
     */
    public BigDecimal getPrice(Order order) {
        if (order.getQuantity() <= 0) {
            throw new IllegalQuantityException(order.getQuantity());
        }

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/price")
                        .queryParam("symbol", order.getSymbol())
                        .build())
                .retrieve()
                .bodyToMono(PriceResponse.class)
                .map(PriceResponse::getPrice)
                .block();
    }
}