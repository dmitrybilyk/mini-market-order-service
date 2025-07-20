package com.minimarket.orders.orderservice.service.impl;

import com.minimarket.orders.orderservice.repository.ExecutionRepository;
import com.minimarket.orders.orderservice.model.Execution;
import com.minimarket.orders.orderservice.service.api.ExecutionService;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link ExecutionService} interface for managing order executions.
 * This service handles the persistence of execution records using the provided repository.
 */
@Service
public class ExecutionServiceImpl implements ExecutionService {

    private final ExecutionRepository executionRepository;

    /**
     * Constructs an {@code ExecutionServiceImpl} instance with the necessary repository.
     *
     * @param executionRepository the repository for performing CRUD operations on executions
     */
    public ExecutionServiceImpl(ExecutionRepository executionRepository) {
        this.executionRepository = executionRepository;
    }

    /**
     * Saves an execution record to the repository.
     *
     * @param execution the {@link Execution} object to be saved
     */
    @Override
    public void saveExecution(Execution execution) {
        executionRepository.save(execution);
    }
}