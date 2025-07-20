//package com.minimarket.orders.orderservice;
//
//import com.minimarket.orders.orderservice.dto.PriceResponse;
//import com.minimarket.orders.orderservice.exception.IllegalQuantityException;
//import com.minimarket.orders.orderservice.model.Order;
//import com.minimarket.orders.orderservice.service.price.PriceService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//import java.math.BigDecimal;
//import java.util.function.Function;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
///**
// * Unit tests for the {@link PriceService} class.
// * Verifies the behavior of the price retrieval functionality using mocked WebClient interactions.
// */
//@ExtendWith(MockitoExtension.class)
//class PriceServiceTest {
//
//    @Mock
//    private WebClient webClient;
//
//    @Mock
//    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
//
//    @Mock
//    private WebClient.RequestHeadersSpec requestHeadersSpec;
//
//    @Mock
//    private WebClient.ResponseSpec responseSpec;
//
//    @InjectMocks
//    private PriceService priceService;
//
//    private Order sampleOrder;
//    private PriceResponse samplePriceResponse;
//
//    /**
//     * Sets up test data and mock configurations before each test.
//     * Initializes a sample order and price response, and configures the mocked WebClient chain.
//     */
//    @BeforeEach
//    void setUp() {
//        sampleOrder = Order.builder()
//                .symbol("AAPL")
//                .quantity(10)
//                .build();
//
//        samplePriceResponse = new PriceResponse();
//        samplePriceResponse.setPrice(new BigDecimal("210.55"));
//        samplePriceResponse.setSymbol("AAPL");
//
//        // Mock WebClient chain
//        when(webClient.get()).thenReturn(requestHeadersUriSpec);
//        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
//        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
//        when(responseSpec.bodyToMono(PriceResponse.class)).thenReturn(Mono.just(samplePriceResponse));
//    }
//
//    /**
//     * Tests the {@link PriceService#getPrice(Order)} method for a valid order.
//     * Verifies that the correct price is returned and the WebClient is called with the expected parameters.
//     */
//    @Test
//    void testGetPrice_Success() {
//        // Act
//        BigDecimal price = priceService.getPrice(sampleOrder);
//
//        // Assert
//        assertNotNull(price);
//        assertEquals(new BigDecimal("210.55"), price);
//        verify(webClient).get();
//        verify(requestHeadersUriSpec).uri(any(Function.class));
//        verify(requestHeadersSpec).retrieve();
//        verify(responseSpec).bodyToMono(PriceResponse.class);
//    }
//
//    /**
//     * Tests the {@link PriceService#getPrice(Order)} method when the order has an invalid quantity (zero or negative).
//     * Verifies that an {@link IllegalQuantityException} is thrown and no WebClient call is made.
//     */
//    @Test
//    void testGetPrice_InvalidQuantity_ThrowsException() {
//        // Arrange
//        sampleOrder.setQuantity(0);
//
//        // Act & Assert
//        IllegalQuantityException exception = assertThrows(IllegalQuantityException.class, () -> {
//            priceService.getPrice(sampleOrder);
//        });
//        assertEquals("Invalid quantity: 0", exception.getMessage());
//        verify(webClient, never()).get();
//    }
//}