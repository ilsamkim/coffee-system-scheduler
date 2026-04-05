package com.example.coffeesystemscheduler.domain.order.scheduler;

import com.example.coffeesystemscheduler.domain.order.entity.OrderHistory;
import com.example.coffeesystemscheduler.domain.order.infrastructrue.DataPlatformCollector;
import com.example.coffeesystemscheduler.domain.order.repository.OrderHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderRetryScheduler {

    private final OrderHistoryRepository orderHistoryRepository;
    private final DataPlatformCollector dataPlatformCollector;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void retryFailedOrders() {
        List<OrderHistory> failedHistories = orderHistoryRepository.findAllByStatus(OrderHistory.HistoryStatus.FAIL);

        if (failedHistories.isEmpty()) return;

        log.info("재전송 전용 서버: 실패 데이터 {}건 처리 시작", failedHistories.size());

        for (OrderHistory history : failedHistories) {
            try {
                dataPlatformCollector.sendOrderInfo(history.getUserId(), history.getCoffeeId(), history.getAmount());
                history.complete();
            } catch (Exception e) {
                history.fail();
            }
        }
    }
}
