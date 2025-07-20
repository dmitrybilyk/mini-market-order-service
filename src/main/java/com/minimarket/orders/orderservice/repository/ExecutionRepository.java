package com.minimarket.orders.orderservice.repository;

import com.minimarket.orders.orderservice.model.Execution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExecutionRepository extends JpaRepository<Execution, Long> {
}
