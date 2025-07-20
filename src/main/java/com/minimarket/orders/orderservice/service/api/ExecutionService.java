package com.minimarket.orders.orderservice.service.api;

import com.minimarket.orders.orderservice.model.Execution;
import org.springframework.stereotype.Service;

@Service
public interface ExecutionService {
    void saveExecution(Execution execution);
}
