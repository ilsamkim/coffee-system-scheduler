package com.example.coffeesystemscheduler.domain.order.repository;

import com.example.coffeesystemscheduler.domain.order.entity.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    List<OrderHistory> findAllByStatus(OrderHistory.HistoryStatus status);
}
