package com.minimarket.orders.orderservice.dto;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PriceResponse {

    private String symbol;
    private BigDecimal price;

}
