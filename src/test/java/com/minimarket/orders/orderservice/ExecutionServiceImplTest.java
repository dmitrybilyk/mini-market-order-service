package com.minimarket.orders.orderservice;

import com.minimarket.orders.orderservice.model.Execution;
import com.minimarket.orders.orderservice.repository.ExecutionRepository;
import com.minimarket.orders.orderservice.service.impl.ExecutionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class ExecutionServiceImplTest {

    private ExecutionRepository executionRepository;
    private ExecutionServiceImpl executionService;

    @BeforeEach
    void setUp() {
        executionRepository = mock(ExecutionRepository.class);
        executionService = new ExecutionServiceImpl(executionRepository);
    }

    @Test
    void testSaveExecution_delegatesToRepository() {
        // given
        Execution execution = new Execution();
        // when
        executionService.saveExecution(execution);
        // then
        verify(executionRepository, times(1)).save(execution);
    }
}
