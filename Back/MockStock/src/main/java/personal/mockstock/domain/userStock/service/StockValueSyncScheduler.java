package personal.mockstock.domain.userStock.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 주식 평가액 동기화 스케줄러
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StockValueSyncScheduler {

    private final UserStockService userStockService;

    /**
     * 서버 시작 시 기존 데이터 정리 (한 번만 실행)
     */
    @PostConstruct
    public void initializeExistingData() {
        log.info("=== 서버 시작: 기존 사용자 stockMoney 초기화 ===");

        try {
            userStockService.updateAllUsersStockValue();
            log.info("=== 기존 데이터 초기화 완료 ===");
        } catch (Exception e) {
            log.error("=== 기존 데이터 초기화 실패 ===", e);
        }
    }

    /**
     * 매일 오후 4시 35분에 모든 사용자 주식 평가액 업데이트
     */
    @Scheduled(cron = "0 35 16 * * MON-FRI")
    public void syncAllUsersStockValue() {
        log.info("=== 스케줄러: 전체 사용자 주식 평가액 동기화 시작 ===");

        try {
            userStockService.updateAllUsersStockValue();
            log.info("=== 스케줄러: 전체 사용자 주식 평가액 동기화 완료 ===");
        } catch (Exception e) {
            log.error("=== 스케줄러: 전체 사용자 주식 평가액 동기화 실패 ===", e);
        }
    }
}